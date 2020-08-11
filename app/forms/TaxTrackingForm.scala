package forms

import models.{InvoiceId, TaxId}
import org.joda.time.DateTime
import play.api.libs.json.{Json, OFormat, Reads}
import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._

object TaxTrackingForm {
  case class TaxTrackingForm(
                            taxTracking: TaxTracking,
                            invoiceTaxTracking: Seq[InvoiceId]
                            )
  case class TaxTracking(
                              id: Option[TaxId],
                              period: String,
                              taxAmount: BigDecimal,
                              paymentDate: DateTime
                            )

  object TaxTracking {
    implicit val formatter: Reads[TaxTracking] = Json.reads[TaxTracking]
  }

  implicit val formatter: Reads[TaxTrackingForm] = Json.reads[TaxTrackingForm]
}
