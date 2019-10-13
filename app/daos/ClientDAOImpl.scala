package daos


import daos.tables.{ClientDAOTables, UserDAOTables}
import javax.inject.Inject
import models.{DBClient, UserID}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}

class ClientDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends ClientDAO with ClientDAOTables with UserDAOTables {

  import profile.api._

  def find(clientId: String): Future[Option[DBClient]] = {
    db.run(slickClient.filter(_.id === clientId).result.headOption).map { result =>
      result.map { res =>
        DBClient(
          res.id,
          res.companyName,
          res.referentFirstName,
          res.referentLastName,
          res.address,
          res.postalCode,
          res.city,
          res.email,
          res.phoneNumber,
          res.VATNumber,
          res.isActive,
          res.userId
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
      address = client.address,
      postalCode = client.postalCode,
      city = client.city,
      email = client.email,
      phoneNumber = client.phoneNumber,
      VATNumber = client.VATNumber,
      isActive = client.isActive,
      userId = client.userId
    )
    db.run(slickClient.insertOrUpdate(dbClient)).map(_ => client)
  }

  def findAll(userId: UserID): Future[Seq[DBClient]] = {
    db.run(slickClient.filter(_.userId === userId).result).map { clients =>
      clients.map { client =>
        DBClient(
          id = client.id,
          companyName = client.companyName,
          referentFirstName = client.referentFirstName,
          referentLastName = client.referentLastName,
          address = client.address,
          postalCode = client.postalCode,
          city = client.city,
          email = client.email,
          phoneNumber = client.phoneNumber,
          VATNumber = client.VATNumber,
          isActive = client.isActive,
          userId = client.userId
        )
      }
    }
  }

  def delete(clientId: String): Future[Int] = {
    db.run(slickClient.filter(_.id === clientId).delete)
  }
}



