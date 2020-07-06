package controllers

import java.util.UUID

import com.builtamont.play.pdf.PdfGenerator
import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.{BankDAO, InvoiceDAO, UserDAO}
import forms.InvoiceForm.InvoiceForm
import javax.inject.Inject
import models._
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import org.joda.time.{DateTime, LocalDateTime}
import play.api.{Configuration, Environment}
import play.api.libs.json.JodaWrites._
import play.api.libs.json.JodaReads._
import play.api.libs.json._
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents, Result}
import utils.auth.{DefaultEnv, WithProvider}

import scala.concurrent.{ExecutionContext, Future}

class InvoiceController @Inject()(silhouette: Silhouette[DefaultEnv],
                                  components: ControllerComponents,
                                  env: Environment,
                                  config: Configuration,
                                  credentialsProvider: CredentialsProvider,
                                  invoiceDAO: InvoiceDAO,
                                  userDAO: UserDAO,
                                  bankDAO: BankDAO
                                 )
                                 (implicit
                                  assets: AssetsFinder,
                                  ex: ExecutionContext)
  extends AbstractController(components)  {

  val pdfGen = new PdfGenerator(env)
  pdfGen.loadLocalFonts(Seq(
    "fonts/Roboto-Black.ttf",
    "fonts/Roboto-BlackItalic.ttf",
    "fonts/Roboto-Bold.ttf",
    "fonts/Roboto-BoldItalic.ttf",
    "fonts/Roboto-Italic.ttf",
    "fonts/Roboto-Light.ttf",
    "fonts/Roboto-LightItalic.ttf",
    "fonts/Roboto-Medium.ttf",
    "fonts/Roboto-MediumItalic.ttf",
    "fonts/Roboto-Regular.ttf",
    "fonts/Roboto-Thin.ttf",
    "fonts/Roboto-ThinItalic.ttf"
  ))

  def createUniqueChronologicalNumber(userID: UserID): Future[Seq[String]] = {
    val actualYear = LocalDateTime.now().getYear.toString
    invoiceDAO.findLastNumberForUser(userID).map { numberList =>
      val number = numberList.collect{ case n if n.number.startsWith(actualYear) => n}.length
      numberList.reverse.map { x =>
        val year: Array[String] = x.number.split('-')
        val yearToLong: Long = year(0).toLong
       if (actualYear.toLong > yearToLong) {
          actualYear + '-' + "%04d".format(1)
        } else {
          val numb = number + 1
          yearToLong.toString + '-' + "%04d".format(numb)
        }
      }
    }
  }

  def getLastInvoiceNumber: Future[Long] = {
    invoiceDAO.findLastNumber.map { list =>
      list.lastOption.map { number =>
        number.id.value
      }.getOrElse(0L)
    }
  }

  def createInvoice(clientId: String, userID: UserID): Action[JsValue] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit req: SecuredRequest[DefaultEnv, JsValue] =>
    req.body.validate[InvoiceForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      invoiceForm => {
        createUniqueChronologicalNumber(userID).flatMap { number =>
          val dbInvoice = DBInvoice(
            publicId = UUID.randomUUID().toString,
            date = DateTime.now(),
            period = invoiceForm.period,
            number = number.headOption.getOrElse(LocalDateTime.now().getYear.toString + '-' + "%04d".format(1)),
            clientId = clientId,
            userID = userID
          )
          getLastInvoiceNumber.flatMap { number =>
            val numb  = number + 1
            val services = invoiceForm.services.map { newService =>
              DBService(
                serviceId = ServiceId(UUID.randomUUID().toString),
                invoiceId = InvoiceId(numb),
                serviceNumber = newService.serviceNumber,
                serviceName = newService.serviceName,
                quantity = newService.quantity,
                unitPrice = newService.unitPrice,
                VATRate = newService.VATRate,
              )
            }
            invoiceDAO.saveInvoice(dbInvoice, services).map(_ => Ok)
          }
        }
      }
    )
  }

  def getInvoices(invoiceId: InvoiceId): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    invoiceDAO.getInvoice(invoiceId).map { invoices =>
      val q = invoices.map { invoice =>
        val x: FullInvoice = FullInvoice.getCalculatedInvoice(invoice)
        Json.toJson(x)
      }
      Ok(Json.toJson(q))
    }
  }

  def findInvoice(invoiceId: InvoiceId): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    invoiceDAO.findInvoice(invoiceId).map { invoices =>
      val data = Json.toJson(invoices)
      Ok(Json.toJson(data))
    }
  }

  def findAllInvoiceByClient(clientId: String): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    invoiceDAO.findInvoiceByClient(clientId).map { DBInvoice =>
      val data = Json.toJson(DBInvoice)
      Ok(Json.toJson(data))
    }
  }

  object Joda {
    implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isAfter _)
  }

  def findAllInvoicesWithClient(userID: UserID): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit req: SecuredRequest[DefaultEnv, AnyContent] =>
    import Joda._

    invoiceDAO.findAllInvoicesWithClient(userID).map { seqInvoiceWithClient =>
      val x = seqInvoiceWithClient.sortBy(_.date).map { invoiceWithClient =>
        val fullInvoice = FullInvoiceWithClient.getCalculatedInvoice(invoiceWithClient)
        Json.obj(
          "publicId" -> fullInvoice.publicId,
          "date" -> fullInvoice.date,
          "period" -> fullInvoice.period,
          "number" -> fullInvoice.number,
          "client" -> fullInvoice.client.companyName,
          "totalTTC" -> fullInvoice.totalTTC
        )
      }
      Ok(Json.toJson(x))
    }
  }

  def exportInvoiceToPdf(publicId: String): Action[AnyContent] = Action.async { implicit r =>
    invoiceDAO.findCompleteInvoice(publicId).flatMap { invoiceSeq =>
      val invoice = invoiceSeq.head
      val client = invoice.client
      val services = invoice.services.map(_.toService)
      val fullInvoice: FullInvoiceWithClient = FullInvoiceWithClient.getCalculatedInvoice(invoice)
      userDAO.find(invoice.userID).flatMap { userOpt =>
        userOpt.map { user =>
          bankDAO.find(user.userID).map { bankOpt =>
            bankOpt.map { bank =>
              pdfGen.ok(views.html.exportPDFInvoice(fullInvoice, client, services.sortBy(_.serviceNumber), user, bank), config.get[String]("baseUrl"))
            }.getOrElse(BadRequest)
          }
        }.getOrElse(Future.successful(BadRequest))
      }
    }
  }
}
