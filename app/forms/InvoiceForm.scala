package forms

import forms.ServiceForm.ServiceForm
import play.api.libs.json._

object InvoiceForm {
  case class InvoiceForm(period: String, services: Seq[ServiceForm])

  object InvoiceForm {
    implicit val formatter: OFormat[InvoiceForm] = Json.format[InvoiceForm]
  }
}
