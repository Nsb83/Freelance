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
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents}
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

  def newClient = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue]  =>
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

  private def makeJsonClient(client: DBClient): JsValue = {
    Json.toJson(Json.obj(
      "id" -> client.id,
      "companyName" -> client.companyName,
      "referentFirstName" -> client.referentFirstName,
      "referentLastName" -> client.referentLastName,
      "address" -> client.address,
      "postalCode" -> client.postalCode,
      "city" -> client.city,
      "email" -> client.email,
      "phoneNumber" -> client.phoneNumber,
      "VATNumber" -> client.VATNumber,
      "isActive" -> client.isActive
    ))
  }

  def findAll(userID: UserID) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.findAll(userID).map { clients =>
      val data = clients.map(makeJsonClient)
      Ok(Json.toJson(data))
    }
  }

  def find(id: String) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.find(id).flatMap {
      case None => Future.successful(BadRequest)
      case Some(clients) => Future.successful(Ok(Json.toJson(clients)))
    }
  }

  def delete(id: String) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    clientDAO.delete(id).map(result => Accepted)
  }
}

