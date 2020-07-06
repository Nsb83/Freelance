package controllers

import com.mohiva.play.silhouette.api.Silhouette
import com.mohiva.play.silhouette.api.actions.SecuredRequest
import com.mohiva.play.silhouette.impl.providers.CredentialsProvider
import daos.BankDAO
import forms.BankForm.BankForm
import javax.inject.Inject
import models.{BankID, DBBank, UserID}
import play.api.libs.json.{JsError, JsValue, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
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

  def saveBank(userId: UserID): Action[JsValue] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async(parse.json) { implicit request: SecuredRequest[DefaultEnv, JsValue]  =>
    request.body.validate[BankForm].fold(
      error => Future.successful(BadRequest(Json.obj("status" -> "KO", "message" -> JsError.toJson(error)))),
      newBankForm => {
        val newBank = DBBank(
          id = newBankForm.id.getOrElse(BankID(0L)),
          bankName = newBankForm.bankName,
          BICNumber = newBankForm.BICNumber,
          IBANNumber = newBankForm.IBANNumber,
          userId = userId
        )
        bankDAO.save(newBank).map(_ => Ok)
      }
    )
  }
  def findMyBank(userId: UserID): Action[AnyContent] = silhouette.SecuredAction(WithProvider[DefaultEnv#A](CredentialsProvider.ID)).async { implicit request: SecuredRequest[DefaultEnv, AnyContent]  =>
    bankDAO.find(userId).map { bankOpt =>
      val myBank = bankOpt.map { bank =>
        Json.obj(
          "id" -> bank.id,
          "bankName" -> bank.bankName,
          "BICNumber" -> bank.BICNumber,
          "IBANNumber" -> bank.IBANNumber
        )
      }
      Ok(Json.toJson(myBank))
    }
  }

}
