package daos.tables

import daos.DAOSlick
import org.joda.time.DateTime
import com.github.tototoshi.slick.MySQLJodaSupport._
import models.{DBIncomeTaxTracking, DBTaxTracking, InvoiceId, TaxId}

trait TaxTrackingDAOTables extends DAOSlick {

  import profile.api._

  class TaxTrackingDBTable(tag: Tag) extends Table[DBTaxTracking](tag, "TaxTracking") {
    def id = column[TaxId]("id", O.PrimaryKey, O.AutoInc)
    def period = column[String]("period")
    def taxAmount = column[BigDecimal]("taxAmount")
    def paymentDate = column[DateTime]("paymentDate")

    def * = (id, period, taxAmount, paymentDate) <> ((DBTaxTracking.apply _).tupled, DBTaxTracking.unapply)
  }

  class InvoiceTaxTrackingDBTable(tag: Tag) extends Table[DBIncomeTaxTracking](tag, "InvoiceTaxTracking") {
    def taxId = column[TaxId]("taxId")
    def invoiceId = column[InvoiceId]("invoiceId")

    def * = (taxId, invoiceId) <> ((DBIncomeTaxTracking.apply _).tupled, DBIncomeTaxTracking.unapply)
  }

  lazy val slickTaxTracking = TableQuery[TaxTrackingDBTable]
  lazy val slickIncomeTaxTracking = TableQuery[InvoiceTaxTrackingDBTable]

}
