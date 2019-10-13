package daos

import models._

import scala.collection.immutable
import scala.concurrent.Future

trait InvoiceDAO {

  def saveInvoice(invoice: DBInvoice, services: Seq[DBService]): Future[Seq[Int]]

  def checkIfZero: Future[Int]

  def findLastNumberForUser(userID: UserID): Future[Seq[DBInvoice]]

  def findLastNumber: Future[Seq[DBInvoice]]

  def findInvoiceByClient(clientId: String): Future[Seq[DBInvoice]]

  def findInvoice(invoiceId: InvoiceId): Future[immutable.Iterable[Invoice]]

  def getInvoice(invoiceId: InvoiceId): Future[Seq[Invoice]]

  def findAllInvoicesWithClient(userID: UserID): Future[Seq[InvoiceWithClient]]

  def findCompleteInvoice(publicId: String): Future[Seq[InvoiceWithClient]]

}
