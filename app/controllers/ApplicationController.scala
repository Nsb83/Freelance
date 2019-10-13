package controllers

import com.mohiva.play.silhouette.api.{Environment, LogoutEvent, Silhouette}
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import forms.{SignInForm, SignUpForm}
import javax.inject.Inject
import models.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import utils.auth.DefaultEnv

import scala.concurrent.Future

class ApplicationController @Inject() (
                                        components: ControllerComponents,
                                        silhouette: Silhouette[DefaultEnv]
                                      )
                                      (
                                        implicit
                                        assets: AssetsFinder
                                      ) extends AbstractController(components) with I18nSupport {

  /**
    * Handles the index action.
    *
    * @return The result to display.
    */
  def index: Action[AnyContent] = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    Future.successful(Ok(views.html.home(request.identity)))
  }

  /**
    * Handles the Sign In action.
    *
    * @return The result to display.
    */
  def signIn: Action[AnyContent] = silhouette.UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
      case None => Future.successful(Ok(views.html.signIn(SignInForm.form)))
    }
  }

  /**
    * Handles the Sign Up action.
    *
    * @return The result to display.
    */
  def signUp: Action[AnyContent] = silhouette.UserAwareAction.async { implicit request =>
    request.identity match {
      case Some(user) => Future.successful(Redirect(routes.ApplicationController.index()))
      case None => Future.successful(Ok(views.html.signUp(SignUpForm.form)))
    }
  }

  /**
    * Handles the Sign Out action.
    *
    * @return The result to display.
    */
  def signOut: Action[AnyContent] = silhouette.SecuredAction.async { implicit request: SecuredRequest[DefaultEnv, AnyContent] =>
    val result = Redirect(routes.ApplicationController.index())
    silhouette.env.eventBus.publish(LogoutEvent(request.identity, request))
    silhouette.env.authenticatorService.discard(request.authenticator, result)
  }

}
