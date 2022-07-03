package daos

import daos.tables.{BankDAOTables, UserDAOTables}
import javax.inject.Inject
import models.{BankID, DBBank, UserID}
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class BankDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends BankDAO with BankDAOTables with UserDAOTables {

  import profile.api._

  def save(bank: DBBank): Future[Option[DBBank]] = {
    val action = slickBank.insertOrUpdate(bank).map(_ => bank)

    db.run(action.asTry).map {
      case Failure(exception) => println(exception.getMessage)
        None
      case Success(value) => Some(value)
    }
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
