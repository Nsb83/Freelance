package forms

import models.UserID
import play.api.libs.json.{Json, OFormat}

object ClientForm {
  case class ClientForm(companyName: String,
                        referentFirstName: String,
                        referentLastName: String,
                        adress: String,
                        postalCode: String,
                        city: String,
                        email : String,
                        phoneNumber: String,
                        VATNumber: String,
                        isActive: Boolean,
                        userId: UserID
                       )
  object ClientForm {
    implicit val formatter: OFormat[ClientForm] = Json.format[ClientForm]
  }
}
