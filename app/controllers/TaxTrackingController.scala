package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.{BankDAO, InvoiceDAO, TaxTrackingDAO, UserDAO}
import forms.TaxTrackingForm.TaxTrackingForm
import javax.inject.Inject
import models.{DBIncomeTaxTracking, DBTaxTracking, InvoiceId, TaxId}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.{Configuration, Environment}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import utils.auth.{DefaultEnv, WithProvider}
import play.api.libs.json.JodaReads._

import scala.concurrent.{ExecutionContext, Future}

class TaxTrackingController @Inject()(silhouette: Silhouette[DefaultEnv],
                                      components: ControllerComponents,
                                      env: Environment,
                                      config: Configuration,
                                      credentialsProvider: CredentialsProvider,
                                      taxTrackingDAO: TaxTrackingDAO

                                     )
                                     (implicit
                                      assets: AssetsFinder,
                                      ex: ExecutionContext)
  extends AbstractController(components)  {


  def saveTaxPayment(): Action[JsValue] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit req =>
    req.body.validate[TaxTrackingForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      taxForm => {
        val taxTrack = DBTaxTracking(
          id = taxForm.taxTracking.id.getOrElse(TaxId(0L)),
          period = taxForm.taxTracking.period,
          taxAmount = taxForm.taxTracking.taxAmount,
          paymentDate = taxForm.taxTracking.paymentDate
        )

        taxTrackingDAO.save(taxTrack, taxForm.invoiceTaxTracking).map(_ => Created)
      }
    )

  }


}
