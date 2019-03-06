package forms

import play.api.libs.json.{Json, OFormat}

object ClientForm {
  case class ClientForm(companyName: String,
                        referentFirstName: String,
                        referentLastName: String,
                        email : String,
                        phoneNumber: String,
                        isActive : Boolean
                       )
  object ClientForm {
    implicit val formatter: OFormat[ClientForm] = Json.format[ClientForm]
  }
}
