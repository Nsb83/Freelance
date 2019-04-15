package controllers

import java.util.UUID

import daos.InvoiceDAO
import forms.InvoiceForm.InvoiceForm
import javax.inject.Inject
import models._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime, LocalDateTime}
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}

import scala.concurrent.{ExecutionContext, Future}

class InvoiceController @Inject()(components: ControllerComponents, invoiceDAO: InvoiceDAO)(implicit val executionContext: ExecutionContext)
  extends AbstractController(components) {

  def createUniqueChronologicalNumber(): Future[String] = {
    val actualYear = LocalDateTime.now().getYear.toString
    invoiceDAO.findLastNumber.map { number =>
      number.lastOption.map { x =>
        val number = x.id.value
          val year: Array[String] = x.number.split('-')
          val yearToLong: Long = year(0).toLong
          if (actualYear.toLong > yearToLong) {
            actualYear + '-' + "%04d".format(1)
          } else {
            val numb = number + 1
            yearToLong.toString + '-' + "%04d".format(numb)
          }

      }.getOrElse(actualYear + '-' + "%04d".format(1))
    }
  }
  def getLastInvoiceNumber(): Future[Long] = {
    invoiceDAO.findLastNumber.map { list =>
      list.lastOption.map { number =>
        number.id.value
      }.getOrElse(0l)
    }
  }

  /*
  Pour le numéro :
    Le reset à chaque nouveau début d'année
  */

  def createInvoice(clientId: String) = Action.async(parse.json) { implicit req =>
    req.body.validate[InvoiceForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      invoiceForm => {
        // Récupérer le dernier id de DBInvoice
        createUniqueChronologicalNumber().flatMap { number =>
          val dbInvoice = DBInvoice(
            publicId = UUID.randomUUID().toString,
            date = DateTime.now(),
            number = number,
            clientId = clientId
          )
          getLastInvoiceNumber.flatMap { number =>
            val numb  = number + 1
            val services = invoiceForm.services.map { newService =>
              DBService(
                serviceId = ServiceId(UUID.randomUUID().toString),
                invoiceId = InvoiceId(numb),
                serviceName = newService.serviceName,
                quantity = newService.quantity,
                unitPrice = newService.unitPrice,
                VATRate = newService.VATRate
              )
            }
            invoiceDAO.saveInvoice(dbInvoice, services).map(_ => Ok)
          }
        }
      }
    )
  }

//  def findAllCompleteInvoiceByClient(clientId: String) = Action.async {
//    invoiceDAO.findCompleteInvoiceByClient(clientId).map { invoices =>
//      val data = makeCompleteInvoicesToJson(invoices)
//      Ok(Json.toJson(data))
//    }
//  }

  def getInvoices(invoiceId: InvoiceId): Action[AnyContent] = Action.async {
    invoiceDAO.getInvoice(invoiceId).map { invoices =>
      val q = invoices.map { invoice =>
        val x: FullInvoice = FullInvoice.getCalculatedInvoice(invoice)
        Json.toJson(x)

      }
      Ok(Json.toJson(q))
    }
  }

  def findInvoice(invoiceId: InvoiceId) = Action.async {
    invoiceDAO.findInvoice(invoiceId).map { invoices =>
      val data = Json.toJson(invoices)
      Ok(Json.toJson(data))
    }
  }

  private def makeCompleteInvoiceToJson(invoice: FullInvoice): JsValue = {
    Json.toJson(invoice)
  }

  def findAllInvoiceByClient(clientId: String) = Action.async {
    invoiceDAO.findInvoiceByClient(clientId).map { DBInvoice =>
      val data = Json.toJson(DBInvoice)
      Ok(Json.toJson(data))
    }
  }

  def findAllInvoices: Action[AnyContent] = Action.async {
    invoiceDAO.findAllInvoices.map { seqInvoice =>
      val q = seqInvoice.map { invoice =>
        val x = FullInvoice.getCalculatedInvoice(invoice)
        Json.toJson(x)
      }
      Ok(Json.toJson(q))
    }
  }

}
