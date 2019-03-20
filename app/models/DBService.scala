package models

import ai.x.play.json.Jsonx
import play.api.libs.json._
import slick.lifted.MappedTo

case class DBService (serviceId: ServiceId,
                      invoiceId: InvoiceId,
                      serviceName: String,
                      quantity: BigDecimal,
                      unitPrice: BigDecimal,
                      VATRate: BigDecimal,
                      totalDutyFreePrice: BigDecimal,
                      VATTotal: BigDecimal,
                      totalPrice: BigDecimal)

case class ServiceId(value: String) extends AnyVal with MappedTo[String]

object ServiceId {
  implicit val formatter: Format[ServiceId] = Jsonx.formatAuto[ServiceId]
}

object DBService {
  implicit val formatter: OFormat[DBService] = Json.format[DBService]
//  implicit val Seqformatter: OFormat[Seq[DBService]] = Json.format[Seq[DBService]]
}






