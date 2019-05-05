package models

import java.util.UUID

import com.mohiva.play.silhouette.api.{Identity, LoginInfo}


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
                 userID: UUID,
                 loginInfo: LoginInfo,
                 firstName: String,
                 lastName: String,
                 fullName: String,
                 email: String,
                 phoneNumber: String,
                 SIRENNumber: String) extends Identity

case class DBUser(
                   userID: String,
                   firstName: String,
                   lastName: String,
                   fullName: String,
                   email: String,
                   phoneNumber: String,
                   SIRENNumber: String)

