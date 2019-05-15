package daos

import models.{DBClient, UserID}

import scala.concurrent.Future

trait ClientDAO  {

  def find(clientId: String): Future[Option[DBClient]]
  def save(client: DBClient) : Future[DBClient]
  def findAll(userID: UserID): Future[Seq[DBClient]]
  def delete(clientId: String): Future[Int]
}
