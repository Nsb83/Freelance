package models

import ai.x.play.json.Jsonx
import play.api.libs.json._
import slick.lifted.MappedTo
import play.api.mvc.PathBindable

case class Service (serviceId: ServiceId,
                    invoiceId: InvoiceId,
                    serviceNumber: Int,
                      serviceName: String,
                      quantity: BigDecimal,
                      unitPrice: BigDecimal,
                      VATRate: BigDecimal,
                      totalDutyFreePrice: BigDecimal,
                      VATTotal: BigDecimal,
                      totalPrice: BigDecimal)

case class DBService (serviceId: ServiceId,
                      invoiceId: InvoiceId,
                      serviceNumber: Int,
                    serviceName: String,
                    quantity: BigDecimal,
                    unitPrice: BigDecimal,
                    VATRate: BigDecimal) {
  def toService: Service = {
    val realVATRate = VATRate / 100
    val totalDutyFreePrice = unitPrice * quantity
    val VATTotal = totalDutyFreePrice * realVATRate

    Service(
      serviceId = serviceId,
      invoiceId = invoiceId,
      serviceNumber = serviceNumber,
      serviceName = serviceName,
      unitPrice = unitPrice,
      quantity = quantity,
      VATRate = realVATRate,
      totalDutyFreePrice = totalDutyFreePrice,
      VATTotal = VATTotal,
      totalPrice = totalDutyFreePrice + VATTotal
    )
  }
}

case class ServiceId(value: String) extends AnyVal with MappedTo[String]
object ServiceId {
  implicit val formatter: Format[ServiceId] = Jsonx.formatAuto[ServiceId]
  implicit def pathBinder(implicit stringBinder: PathBindable[String]): PathBindable[ServiceId] {
    def bind(key: String, value: String): Either[String, ServiceId]

    def unbind(key: String, value: ServiceId): String
  } = new PathBindable[ServiceId] {
    override def bind(key: String, value: String): Either[String, ServiceId] = {
      for {
        id <- stringBinder.bind(key, value).right
      } yield {
        ServiceId(id)
      }
    }
    override def unbind(key: String, value: ServiceId): String = value.value.toString
  }
}

object DBService {
  implicit val formatter: OFormat[DBService] = Json.format[DBService]
  implicit val writer: OWrites[DBService] = Json.writes[DBService]
}

object Service {
  implicit val formatter: OFormat[Service] = Json.format[Service]
}






