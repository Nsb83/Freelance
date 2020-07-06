package daos

import models.{DBBank, UserID}

import scala.concurrent.Future

trait BankDAO {
  def save(bank: DBBank): Future[Option[DBBank]]
  def find(userID: UserID): Future[Option[DBBank]]

}
