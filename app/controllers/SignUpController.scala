package controllers

import java.util.UUID

import com.mohiva.play.silhouette.api.{Environment, LoginEvent, LoginInfo, SignUpEvent, Silhouette}
import com.mohiva.play.silhouette.api.repositories.AuthInfoRepository
import com.mohiva.play.silhouette.api.util.PasswordHasher
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import forms.SignUpForm
import javax.inject.Inject
import models.{User, UserID}
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.{AbstractController, ControllerComponents}
import services.UserService
import utils.auth.DefaultEnv

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
                                   passwordHasher: PasswordHasher)
                                 ( implicit
                                   assets: AssetsFinder,
                                   ex: ExecutionContext) extends AbstractController(components) with I18nSupport {

  /**
    * Registers a new user.
    *
    * @return The result to display.
    */
  def signUp = silhouette.UnsecuredAction.async { implicit request =>
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
              SIRENNumber = data.SIRENNumber
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
}
