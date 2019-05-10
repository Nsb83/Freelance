package forms

import play.api.libs.json._

object ServiceForm {

  case class ServiceForm(
                         serviceName: String,
                         quantity: BigDecimal,
                         unitPrice: BigDecimal,
                         VATRate: BigDecimal,
                        )

  object ServiceForm {
    implicit val formatter: OFormat[ServiceForm] = Json.format[ServiceForm]
  }
}
