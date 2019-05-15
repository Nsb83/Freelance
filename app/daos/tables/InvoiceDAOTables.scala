package daos.tables

import java.util.UUID

import daos.DAOSlick
import models.{DBInvoice, InvoiceId, UserID}
import org.joda.time.DateTime
import com.github.tototoshi.slick.MySQLJodaSupport._

trait InvoiceDAOTables extends DAOSlick {

  import profile.api._

  class InvoiceDBTable(tag: Tag) extends Table[DBInvoice](tag, "Invoice") {
    def id = column[InvoiceId]("id", O.PrimaryKey, O.AutoInc)
    def publicId = column[String]("publicId")
    def date = column[DateTime]("date")
    def number = column[String]("number")
    def clientId = column[String]("clientId")
    def userId = column[UserID]("userId")
    def * = (
      id,
      publicId,
      date,
      number,
      clientId,
      userId) <> ((DBInvoice.apply _).tupled, DBInvoice.unapply)
  }
  lazy val slickInvoice = TableQuery[InvoiceDBTable]
}
