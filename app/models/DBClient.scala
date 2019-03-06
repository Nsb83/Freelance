package models

import play.api.libs.json.{Json, OFormat}

case class DBClient (id: String,
                     companyName: String,
                     referentFirstName: String,
                     referentLastName: String,
                     email : String,
                     phoneNumber: String,
                     isActive : Boolean)

object DBClient {
  implicit val formatter: OFormat[DBClient] = Json.format[DBClient]
}

