package forms

import forms.ClientForm.ClientForm
import models.{BankID, UserID}
import play.api.libs.json.{Json, OFormat}

object BankForm {
  case class BankForm(
                      id: Option[BankID],
                      bankName: String,
                      BICNumber: String,
                      IBANNumber: String)

  object BankForm {
    implicit val formatter: OFormat[BankForm] = Json.format[BankForm]
  }

}
