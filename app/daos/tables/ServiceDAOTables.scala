package daos.tables

import daos.DAOSlick
import models.{DBService, InvoiceId, ServiceId, Service}

trait ServiceDAOTables extends DAOSlick {

  import profile.api._

  class ServiceDBTable(tag: Tag) extends Table[DBService](tag, "Service") {
    def serviceId = column[ServiceId]("id", O.PrimaryKey)
    def invoiceId = column[InvoiceId]("invoiceId")
    def serviceNumber = column[Int]("serviceNumber")
    def serviceName = column[String]("serviceName")
    def quantity = column[BigDecimal]("quantity")
    def unitPrice = column[BigDecimal]("unitPrice")
    def VATRate = column[BigDecimal]("VATRate")
    def * = (
      serviceId,
      invoiceId,
      serviceNumber,
      serviceName,
      quantity,
      unitPrice,
      VATRate,
) <> ((DBService.apply _).tupled, DBService.unapply)
  }
  lazy val slickService = TableQuery[ServiceDBTable]
}
