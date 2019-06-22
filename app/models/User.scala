package models

import java.util.UUID

import ai.x.play.json.Jsonx
import com.mohiva.play.silhouette.api.{Identity, LoginInfo}
import play.api.libs.json.{Format, Json, OWrites}
import play.api.mvc.PathBindable
import slick.lifted.MappedTo


/**
  * The user object.
  *
  * @param userID The unique ID of the user.
  * @param loginInfo The linked login info.
  * @param firstName Maybe the first name of the authenticated user.
  * @param lastName Maybe the last name of the authenticated user.
  * @param email Maybe the email of the authenticated provider.
  */
case class User(
                 userID: UserID,
                 loginInfo: LoginInfo,
                 firstName: String,
                 lastName: String,
                 fullName: String,
                 email: String,
                 phoneNumber: String,
                 address: String,
                 postalCode: String,
                 city: String,
                 SIRETNumber: String) extends Identity

case class DBUser(
                   userID: UserID,
                   firstName: String,
                   lastName: String,
                   fullName: String,
                   email: String,
                   phoneNumber: String,
                   address: String,
                   postalCode: String,
                   city: String,
                   SIRETNumber: String)

object User {
  implicit val userWriter: OWrites[User] = Json.writes[User]
}

case class UserID(value: String) extends AnyVal with MappedTo[String]
object UserID {
  implicit val formatter: Format[UserID] = Jsonx.formatAuto[UserID]
  implicit def pathBinder(implicit stringBinder: PathBindable[String]): PathBindable[UserID] {
    def bind(key: String, value: String): Either[String, UserID]

    def unbind(key: String, value: UserID): String
  } = new PathBindable[UserID] {
    override def bind(key: String, value: String): Either[String, UserID] = {
      for {
        id <- stringBinder.bind(key, value).right
      } yield {
        UserID(id)
      }
    }
    override def unbind(key: String, value: UserID): String = value.value.toString
  }
}
