package controllers

import java.util.UUID

import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.api.{Environment, LoginEvent, LoginInfo, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.{PasswordHasher, PasswordHasherRegistry}
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.UserDAO
import forms.SignUpForm
import javax.inject.Inject
import models.{Credentials, DBUser, User, UserID, UserToUpdate}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json, Reads}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import services.UserService
import utils.auth.{DefaultEnv, WithProvider}

import scala.concurrent.{ExecutionContext, Future}


/**
  * The sign up controller.
  *
  * @param messagesApi The Play messages API.
  * @param userService The user service implementation.
  * @param authInfoRepository The auth info repository implementation.
  * @param passwordHasher The password hasher implementation.
  */
class SignUpController @Inject() (
                                   messagesApi: MessagesApi,
                                   val env: Environment[DefaultEnv],
                                   components: ControllerComponents,
                                   silhouette: Silhouette[DefaultEnv],
                                   userService: UserService,
                                   authInfoRepository: AuthInfoRepository,
                                   passwordHasherRegistry: PasswordHasherRegistry,
                                   userDAO: UserDAO,
                                   passwordHasher: PasswordHasher)
                                 ( implicit
                                   assets: AssetsFinder,
                                   ex: ExecutionContext) extends AbstractController(components) with I18nSupport {

  /**
    * Registers a new user.
    *
    * @return The result to display.
    */
  def signUp: Action[AnyContent] = silhouette.UnsecuredAction.async { implicit request =>
    SignUpForm.form.bindFromRequest.fold(
      form => Future.successful(BadRequest(views.html.signUp(form))),
      data => {
        val loginInfo = LoginInfo(CredentialsProvider.ID, data.email)
        userService.retrieve(loginInfo).flatMap {
          case Some(user) =>
            Future.successful(Redirect(routes.ApplicationController.signUp()).flashing("error" -> Messages("user.exists")))
          case None =>
            val authInfo = passwordHasher.hash(data.password)
            val user = User(
              userID = UserID(UUID.randomUUID.toString),
              loginInfo = loginInfo,
              firstName = data.firstName,
              lastName = data.lastName,
              fullName = data.firstName + ' ' + data.lastName.toUpperCase(),
              email = data.email,
              phoneNumber = data.phoneNumber,
              address = data.address,
              postalCode = data.postalCode,
              city = data.city,
              SIRETNumber = data.SIRETNumber
            )
            for {
              user <- userService.save(user)
              authInfo <- authInfoRepository.add(loginInfo, authInfo)
              authenticator <- env.authenticatorService.create(loginInfo)
              value <- env.authenticatorService.init(authenticator)
              result <- env.authenticatorService.embed(value, Redirect(routes.ApplicationController.index()))
            } yield {
              env.eventBus.publish(SignUpEvent(user, request))
              env.eventBus.publish(LoginEvent(user, request))
              result
            }
        }
      }
    )
  }

  case class UserToUpdateForm(
                               userID: UserID,
                               firstName: String,
                               lastName: String,
                               email: String,
                               phoneNumber: String,
                               address: String,
                               postalCode: String,
                               city: String,
                               SIRETNumber: String,
                               password: String,
                             ) {
    def toUserToUpdate: UserToUpdate = {
      UserToUpdate(
        user = DBUser(
          userID = this.userID,
          firstName = this.firstName,
          lastName = this.lastName,
          fullName = s"${this.firstName} ${this.lastName}".toLowerCase.split(' ').map(_.capitalize).mkString(" "),
          email = this.email,
          phoneNumber = this.phoneNumber,
          address = this.address,
          postalCode = this.postalCode,
          city = this.city,
          SIRETNumber = this.SIRETNumber
        ),
          credentials = Credentials(
          loginInfo = LoginInfo(CredentialsProvider.ID, this.email),
          passwordInfo = passwordHasherRegistry.current.hash(this.password)
        )
      )
    }
  }

  def updateUser: Action[JsValue] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit req: SecuredRequest[DefaultEnv, JsValue] =>
    implicit val reader: Reads[UserToUpdateForm] = Json.reads[UserToUpdateForm]

    req.body.validate[UserToUpdateForm] match {
      case JsSuccess(userToUpdate, _) =>
        val credentials = userToUpdate.toUserToUpdate.credentials
        val upd = userDAO.update(userToUpdate.toUserToUpdate).map { _ =>
          authInfoRepository.update(credentials.loginInfo, credentials.passwordInfo)
        }
        upd.map(_ => Ok)
      case JsError(e) => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(e))))
    }
  }


}
