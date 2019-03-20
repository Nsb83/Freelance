package models

import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._
import ai.x.play.json.Jsonx
import org.joda.time.DateTime
import play.api.libs.json._
import slick.lifted.MappedTo

case class DBInvoice (id: InvoiceId,
                      date: DateTime,
                      number: String,
                      clientId: String)

case class InvoiceId(value: String) extends AnyVal with MappedTo[String]

object InvoiceId {
  implicit val formatter: Format[InvoiceId] = Jsonx.formatAuto[InvoiceId]
}

object DBInvoice {
  implicit val JodaWrites: Writes[DateTime] = JodaWrites.writes
  implicit val JodaReads: Reads[DateTime] = JodaReads.reads
  implicit val dateTimeFormat: Format[DateTime] = Format(JodaReads, JodaWrites)

  implicit val formatter: OFormat[DBInvoice] = Json.format[DBInvoice]
}

case class Invoice (id: InvoiceId,
                    date: DateTime,
                    number: String,
                    clientId: String,
                    services: Seq[DBService])
object Invoice {
//  implicit val JodaWrites: Writes[DateTime] = JodaWrites.writes
//  implicit val JodaReads: Reads[DateTime] = JodaReads.reads
//  implicit val dateTimeFormat: Format[DateTime] = Format(JodaReads, JodaWrites)

  implicit val formatter: OFormat[Invoice] = Json.format[Invoice]
}

