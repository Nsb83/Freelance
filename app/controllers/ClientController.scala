package controllers

import java.util.UUID

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.ClientDAO
import forms.ClientForm
import javax.inject.Inject
import models.{DBClient, UserID}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import utils.auth.{DefaultEnv, WithProvider}

import scala.concurrent.{ExecutionContext, Future}

class ClientController @Inject()(
                                  components: ControllerComponents,
                                  silhouette: Silhouette[DefaultEnv],
                                  clientDAO: ClientDAO
                                )(implicit
                                assets: AssetsFinder,
                                  val executionContext: ExecutionContext)
  extends AbstractController(components) {

  def newClient: Action[JsValue] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue]  =>
    request.body.validate[ClientForm.ClientForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      clientForm => {
        val clientUUID = UUID.randomUUID()
        val newClient: DBClient = DBClient(
          id = clientUUID.toString,
          companyName = clientForm.companyName,
          referentFirstName = clientForm.referentFirstName,
          referentLastName = clientForm.referentLastName,
          address = clientForm.address,
          postalCode = clientForm.postalCode,
          city = clientForm.city,
          email = clientForm.email,
          phoneNumber = clientForm.phoneNumber,
          VATNumber = clientForm.VATNumber,
          isActive = clientForm.isActive,
          userId = clientForm.userId
        )
        clientDAO.save(newClient).map { _ => Ok
        }
      }
    )
  }

  def findAll(userID: UserID): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.findAll(userID).map { clients =>
      Ok(Json.toJson(clients))
    }
  }

  def find(id: String): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.find(id).flatMap {
      case None => Future.successful(BadRequest)
      case Some(clients) => Future.successful(Ok(Json.toJson(clients)))
    }
  }

  def delete(id: String): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.delete(id).map(_ => Accepted)
  }
}

