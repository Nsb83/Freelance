package controllers

import java.util.UUID

import daos.InvoiceDAO
import forms.InvoiceForm.InvoiceForm
import javax.inject.Inject
import models._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime, LocalDateTime}
import play.api.libs.json.{JsError, JsString, JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

class InvoiceController @Inject()(components: ControllerComponents, invoiceDAO: InvoiceDAO)(implicit val executionContext: ExecutionContext)
  extends AbstractController(components) {

  def createUniqueChronologicalNumber(lastNumber: Int) = {
    val year = LocalDateTime.now().getYear.toString
    year + '-' + "%04d".format(lastNumber)
  }
  /*
  Pour le numéro :
    Le reset à chaque nouveau début d'année
  */

  def createInvoice(clientId: String) = Action.async(parse.json) { implicit req =>
    req.body.validate[InvoiceForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "Vous dites ? ... C'est non !", "message" -> JsError.toJson(error)))),
      invoiceForm => {
        invoiceDAO.findLastNumber.flatMap { lastNumber =>
          val newInvoice = DBInvoice(
            id = InvoiceId(UUID.randomUUID().toString),
            date = DateTime.now(),
            number = createUniqueChronologicalNumber(lastNumber),
            clientId = clientId,
          )
          val dbServices = invoiceForm.services.map { newService =>
            DBService(
              serviceId = ServiceId(UUID.randomUUID().toString),
              invoiceId = newInvoice.id,
              serviceName = newService.serviceName,
              quantity = newService.quantity,
              unitPrice = newService.unitPrice,
              VATRate = newService.VATRate,
              totalDutyFreePrice = newService.totalDutyFreePrice,
              VATTotal = newService.VATTotal,
              totalPrice = newService.totalPrice
            )
          }
          invoiceDAO.saveInvoice(newInvoice, dbServices).map(_ => Ok)
        }
      }
    )
  }

  def findAllCompleteInvoiceByClient(clientId: String) = Action.async {
    invoiceDAO.findCompleteInvoiceByClient(clientId).map { invoices =>
      val data = makeCompleteInvoicesToJson(invoices)
      Ok(Json.toJson(data))
    }
  }

  private def makeCompleteInvoicesToJson(invoices: Seq[Invoice]): JsValue = {
//    val dateTimeFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    Json.toJson(invoices)
  }
}
