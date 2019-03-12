package controllers

import java.util.UUID

import daos.ClientDAO
import forms.ClientForm
import javax.inject.Inject
import models.DBClient
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.{ExecutionContext, Future}

class ClientController @Inject()(components: ControllerComponents, clientDAO: ClientDAO)(implicit val executionContext: ExecutionContext)
  extends AbstractController(components) {

  def newClient = Action.async(parse.json) { implicit req =>
    req.body.validate[ClientForm.ClientForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      clientForm => {
        val clientUUID = UUID.randomUUID()
        val newClient: DBClient = DBClient(
          id = clientUUID.toString,
          companyName = clientForm.companyName,
          referentFirstName = clientForm.referentFirstName,
          referentLastName = clientForm.referentLastName,
          adress = clientForm.adress,
          postalCode = clientForm.postalCode,
          city = clientForm.city,
          email = clientForm.email,
          phoneNumber = clientForm.phoneNumber,
          VATNumber = clientForm.VATNumber,
          isActive = clientForm.isActive
        )
        clientDAO.save(newClient).map { clientForm =>
          Ok
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
      "adress" -> client.adress,
      "postalCode" -> client.postalCode,
      "city" -> client.city,
      "email" -> client.email,
      "phoneNumber" -> client.phoneNumber,
      "VATNumber" -> client.VATNumber,
      "isActive" -> client.isActive
    ))
  }

  def findAll = Action.async { implicit req =>
    clientDAO.findAll().map { clients =>
      val data = clients.map(makeJsonClient)
      Ok(Json.toJson(data))
    }
  }

  def find(id: String) = Action.async { implicit req =>
    clientDAO.find(id).flatMap {
      case None => Future.successful(BadRequest)
      case Some(clients) => Future.successful(Ok(Json.toJson(clients)))
    }
  }

  def delete(id: String) = Action.async {
    clientDAO.delete(id).map(result => Accepted)
  }
}

