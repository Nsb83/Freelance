package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.BankDAO
import forms.BankForm.BankForm
import javax.inject.Inject
import models.{DBBank, UserID}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import utils.auth.{DefaultEnv, WithProvider}

import scala.concurrent.{ExecutionContext, Future}

class BankController @Inject()(
                                components: ControllerComponents,
                                silhouette: Silhouette[DefaultEnv],
                                bankDAO: BankDAO
                              )(implicit
                                assets: AssetsFinder,
                                val executionContext: ExecutionContext)
        extends AbstractController(components) {

  def newBank(userId: UserID) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue]  =>
    request.body.validate[BankForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      newBankForm => {
        val newBank = DBBank(
          bankName = newBankForm.bankName,
          BICNumber = newBankForm.BICNumber,
          IBANNumber = newBankForm.IBANNumber,
          userId = userId
        )
        bankDAO.save(newBank).map(_ => Ok)
      }
    )
  }
  def findMyBank(userId: UserID) = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue]  =>
    bankDAO.find(userId).map { bankOpt =>
      val myBank = bankOpt.map { bank =>
        Json.obj(
          "bankName" -> bank.bankName,
          "BICNumber" -> bank.BICNumber,
          "IBANNumber" -> bank.IBANNumber
        )
      }
      Ok(Json.toJson(myBank))
    }
  }

}
