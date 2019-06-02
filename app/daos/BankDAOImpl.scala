package daos

import daos.tables.{BankDAOTables, UserDAOTables}
import javax.inject.Inject
import models.{BankID, DBBank, UserID}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}

class BankDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends BankDAO with BankDAOTables with UserDAOTables {

  import profile.api._

  def save(bank: DBBank): Future[DBBank] = {
    val dbBank = DBBank(
      id = bank.id,
      bankName = bank.bankName,
      BICNumber = bank.BICNumber,
      IBANNumber = bank.IBANNumber,
      userId = bank.userId
    )
    db.run(slickBank.insertOrUpdate(dbBank).map(_ => bank))
  }


  def find(userID: UserID): Future[Option[DBBank]] = {
    db.run(slickBank.filter(_.userId === userID).result.headOption).map { bankOpt =>
      bankOpt.map { bank =>
        DBBank(
          bank.id,
          bank.bankName,
          bank.BICNumber,
          bank.IBANNumber,
          bank.userId
        )
      }
    }
  }
}
