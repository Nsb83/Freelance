package daos.tables

import java.util.UUID

import daos.DAOSlick
import models.{DBInvoice, InvoiceId}
import org.joda.time.DateTime
import com.github.tototoshi.slick.MySQLJodaSupport._

trait InvoiceDAOTables extends DAOSlick {

  import profile.api._

  class InvoiceDBTable(tag: Tag) extends Table[DBInvoice](tag, "Invoice") {
    def id = column[InvoiceId]("id", O.PrimaryKey)
    def date = column[DateTime]("date")
    def number = column[String]("number")
    def clientId = column[String]("clientId")
    def * = (
      id,
      date,
      number,
      clientId) <> ((DBInvoice.apply _).tupled, DBInvoice.unapply)
  }
  lazy val slickInvoice = TableQuery[InvoiceDBTable]
}
