package forms

import models.{InvoiceId, ServiceId}
import play.api.libs.json.{Json, OFormat}

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
