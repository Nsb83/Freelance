package daos

import models._

import scala.collection.immutable
import scala.concurrent.Future

trait InvoiceDAO {

  def saveInvoice(invoice: DBInvoice, services: Seq[DBService]): Future[Seq[Int]]

  def checkIfZero: Future[Int]

  def findLastNumber: Future[Seq[DBInvoice]]

//  def findCompleteInvoiceByClient(clientId: String): Future[Seq[Invoice]]

  def findInvoiceByClient(clientId: String): Future[Seq[DBInvoice]]

  def findAllInvoices: Future[Seq[Invoice]]

  def findInvoice(invoiceId: InvoiceId): Future[immutable.Iterable[Invoice]]

  def getInvoice(invoiceId: InvoiceId): Future[Seq[Invoice]]
}
