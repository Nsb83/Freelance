package daos.tables

import daos.DAOSlick
import models.{BankID, DBBank, UserID}

trait BankDAOTables extends DAOSlick {

  import profile.api._

  class BankDBTable(tag: Tag) extends Table[DBBank](tag, "Bank") {
    def id = column[BankID]("id", O.PrimaryKey, O.AutoInc)
    def bankName = column[String]("bankName")
    def BICNumber = column[String]("BICNumber")
    def IBANNumber = column[String]("IBANNumber")
    def userId = column[UserID]("userId")
    def * = (id, bankName, BICNumber, IBANNumber, userId) <> ((DBBank.apply _).tupled, DBBank.unapply)
  }
  lazy val slickBank = TableQuery[BankDBTable]
}
