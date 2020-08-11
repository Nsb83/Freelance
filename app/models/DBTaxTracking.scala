package models

import ai.x.play.json.Jsonx
import org.joda.time.DateTime
import play.api.libs.json.Format
import slick.lifted.MappedTo
import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._

case class DBTaxTracking(
                        id: TaxId,
                        period: String,
                        taxAmount: BigDecimal,
                        paymentDate: DateTime
                        )

case class TaxId(value: Long) extends AnyVal with MappedTo[Long]

object TaxId {
  implicit val formatter: Format[TaxId] = Jsonx.formatAuto[TaxId]
}

case class DBIncomeTaxTracking(
                            taxId: TaxId,
                            invoiceId: InvoiceId
                            )