package daos

import models.{DBClient, DBInvoice, DBService, Invoice}

import scala.concurrent.Future

trait InvoiceDAO {

  def saveInvoice(invoice: DBInvoice, services: Seq[DBService]): Future[Seq[Int]]

  def findLastNumber: Future[Int]

  def findCompleteInvoiceByClient(clientId: String): Future[Seq[Invoice]]

  def findInvoiceByClient(clientId: String): Future[Seq[DBInvoice]]

  def findAllInvoices: Future[Seq[DBInvoice]]
}
