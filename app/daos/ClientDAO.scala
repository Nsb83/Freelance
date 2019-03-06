package daos

import models.DBClient

import scala.concurrent.Future

trait ClientDAO  {

  def find(id: String): Future[Option[DBClient]]
  def save(client: DBClient) : Future[DBClient]
  def findAll(): Future[Seq[DBClient]]
  def delete(clientId: String): Future[Int]
}
