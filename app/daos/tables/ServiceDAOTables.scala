package daos.tables

import daos.DAOSlick
import models.{DBService, InvoiceId, ServiceId}

trait ServiceDAOTables extends DAOSlick {

  import profile.api._

  class ServiceDBTable(tag: Tag) extends Table[DBService](tag, "Service") {
    def serviceId = column[ServiceId]("id", O.PrimaryKey)
    def invoiceId = column[InvoiceId]("invoiceId")
    def serviceName = column[String]("serviceName")
    def quantity = column[BigDecimal]("quantity")
    def unitPrice = column[BigDecimal]("unitPrice")
    def VATRate = column[BigDecimal]("VATRate")
    def totalDutyFreePrice = column[BigDecimal]("totalDutyFreePrice")
    def VATTotal = column[BigDecimal]("VATTotal")
    def totalPrice = column[BigDecimal]("totalPrice")
    def * = (
      serviceId,
      invoiceId,
      serviceName,
      quantity,
      unitPrice,
      VATRate,
      totalDutyFreePrice,
      VATTotal,
      totalPrice) <> ((DBService.apply _).tupled, DBService.unapply)
  }
  lazy val slickService = TableQuery[ServiceDBTable]
}
