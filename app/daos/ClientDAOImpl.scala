package daos


import daos.tables.ClientDAOTables
import javax.inject.Inject
import models.DBClient

import scala.concurrent.{ExecutionContext, Future}

class ClientDAOImpl @Inject() (override protected val dbConfigProvider: _root_.play.api.db.slick.DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends ClientDAO with ClientDAOTables {

  import profile.api._

  def find(clientId: String): Future[Option[DBClient]] = {
    db.run(slickClient.filter(_.id === clientId).result.headOption).map { result =>
      result.map { res =>
        DBClient(
          res.id,
          res.companyName,
          res.referentFirstName,
          res.referentLastName,
          res.email,
          res.phoneNumber,
          res.isActive
        )
      }
    }
  }

  def save(client: DBClient) : Future[DBClient] = {
    val dbClient = DBClient(
      id = client.id,
      companyName = client.companyName,
      referentFirstName = client.referentFirstName,
      referentLastName = client.referentLastName,
      email = client.email,
      phoneNumber = client.phoneNumber,
      isActive = client.isActive
    )
    db.run(slickClient.insertOrUpdate(dbClient)).map(_ => client)
  }

  def findAll: Future[Seq[DBClient]] = {
    db.run(slickClient.result)
  }

  def delete(clientId: String): Future[Int] = {
    db.run(slickClient.filter(_.id === clientId).delete)
  }
}



