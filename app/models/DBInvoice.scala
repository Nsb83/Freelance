package models

import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._
import ai.x.play.json.Jsonx
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.PathBindable
import slick.lifted.MappedTo

import scala.concurrent.Future

//changer les id pour un long pour permettre de retrouver le dernier en DB

case class DBInvoice (id: InvoiceId = InvoiceId(0),
                      publicId: String,
                      date: DateTime,
                      number: String,
                      clientId: String)

case class InvoiceId(value: Long) extends AnyVal with MappedTo[Long]

object InvoiceId {
  implicit val formatter: Format[InvoiceId] = Jsonx.formatAuto[InvoiceId]
  implicit def pathBinder(implicit longBinder: PathBindable[Long]): PathBindable[InvoiceId] {
    def bind(key: String, value: String): Either[String, InvoiceId]

    def unbind(key: String, value: InvoiceId): String
  } = new PathBindable[InvoiceId] {
    override def bind(key: String, value: String): Either[String, InvoiceId] = {
      for {
        id <- longBinder.bind(key, value).right
      } yield {
        InvoiceId(id)
      }
    }
    override def unbind(key: String, value: InvoiceId): String = value.value.toString
  }
}

object DBInvoice {
  implicit val formatter: OFormat[DBInvoice] = Json.format[DBInvoice]
}

case class Invoice (id: InvoiceId,
                    publicId: String,
                    date: DateTime,
                    number: String,
                    clientId: String,
                    services: Seq[DBService])


object Invoice {
  implicit val formatter: OFormat[Invoice] = Json.format[Invoice]
}

case class FullInvoice (id: InvoiceId,
                        publicId: String,
                        date: DateTime,
                        number: String,
                        client: String,
                        services: Seq[Service],
                        totalHT: BigDecimal,
                        totalTVAs: Map[BigDecimal, BigDecimal],
                        totalTTC: BigDecimal)

object FullInvoice {
  //  implicit val formatter: OFormat[FullInvoice] = Json.format[FullInvoice]
  implicit val writer: OWrites[FullInvoice] = Json.writes[FullInvoice]

  def getCalculatedInvoice(invoice: Invoice): FullInvoice = {
    val seqServ = invoice.services
    val calculatedService = seqServ.map(_.toService)
    val VATRates: Map[BigDecimal, BigDecimal] = calculatedService.groupBy(_.VATRate).map { case (vat, rate) =>
      (vat, rate.map(_.VATTotal).sum)
    }
    FullInvoice(invoice.id, invoice.publicId, invoice.date, invoice.number, invoice.clientId, calculatedService, calculatedService.map(_.totalDutyFreePrice).sum, VATRates, calculatedService.map(_.totalPrice).sum)
  }
}
case class InvoiceWithClient (id: InvoiceId,
                              publicId: String,
                              date: DateTime,
                              number: String,
                              services: Seq[DBService],
                              client: DBClient)

  object InvoiceWithClient {
    implicit val formatter: OFormat[InvoiceWithClient] = Json.format[InvoiceWithClient]
  }

case class FullInvoiceWithClient (id: InvoiceId,
                        publicId: String,
                        date: DateTime,
                        number: String,
                        client: DBClient,
                        services: Seq[Service],
                        totalHT: BigDecimal,
                        totalTVAs: Map[BigDecimal, BigDecimal],
                        totalTTC: BigDecimal)

object FullInvoiceWithClient {
  implicit val writer: OWrites[FullInvoiceWithClient] = Json.writes[FullInvoiceWithClient]

  def getCalculatedInvoice(invoiceWithClient: InvoiceWithClient): FullInvoiceWithClient = {
    val seqServ: Seq[DBService] = invoiceWithClient.services
    val calculatedService: Seq[Service] = seqServ.map(_.toService)
    val VATRates: Map[BigDecimal, BigDecimal] = calculatedService.groupBy(_.VATRate).map { case (vat, rate) =>
      (vat, rate.map(_.VATTotal).sum)
    }
    FullInvoiceWithClient(invoiceWithClient.id, invoiceWithClient.publicId, invoiceWithClient.date, invoiceWithClient.number, invoiceWithClient.client, calculatedService, calculatedService.map(_.totalDutyFreePrice).sum, VATRates, calculatedService.map(_.totalPrice).sum)
  }
}

