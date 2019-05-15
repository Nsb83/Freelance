package forms

import forms.ServiceForm.ServiceForm
import models.UserID
import play.api.libs.json._

object InvoiceForm {
  case class InvoiceForm(services: Seq[ServiceForm])

  object InvoiceForm {
    implicit val formatter: OFormat[InvoiceForm] = Json.format[InvoiceForm]
  }
}
