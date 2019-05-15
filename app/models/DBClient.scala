package models

import play.api.libs.json.{Json, OFormat}

case class DBClient (id: String,
                     companyName: String,
                     referentFirstName: String,
                     referentLastName: String,
                     adress: String,
                     postalCode: String,
                     city: String,
                     email : String,
                     phoneNumber: String,
                     VATNumber: String,
                     isActive : Boolean,
                     userId: UserID)

object DBClient {
  implicit val formatter: OFormat[DBClient] = Json.format[DBClient]
}

