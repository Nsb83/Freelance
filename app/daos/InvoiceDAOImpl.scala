package daos

import daos.tables.{InvoiceDAOTables, ServiceDAOTables}
import javax.inject.Inject
import models._

import scala.concurrent.{ExecutionContext, Future}

class InvoiceDAOImpl @Inject() (override protected val dbConfigProvider: _root_.play.api.db.slick.DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends InvoiceDAO with InvoiceDAOTables with ServiceDAOTables {

  import profile.api._

  def saveInvoice(invoice: DBInvoice, services: Seq[DBService]): Future[Seq[Int]] = {
    val invoiceSave = (slickInvoice += invoice).flatMap { _ =>
      DBIO.sequence(services.map { service =>
        slickService += service
      })
    }
    db.run(invoiceSave)
  }

  def findLastNumber: Future[Int] = {
    db.run(slickInvoice.length.result)
  }

  def findCompleteInvoiceByClient(clientId: String): Future[Seq[Invoice]] = {
    val query = slickInvoice.filter(_.clientId === clientId)
      .joinLeft(slickService).on(_.id === _.invoiceId)
    db.run(query.result).map { invoices =>
      invoices.groupBy(_._1).map { x =>
        Invoice(
          id = x._1.id,
          date = x._1.date,
          number = x._1.number,
          clientId = x._1.clientId,
          services = x._2.flatMap(_._2)
        )
      }.toSeq
    }
  }
  def findInvoiceByClient(clientId: String): Future[Seq[DBInvoice]] = {
    db.run(slickInvoice.filter(_.clientId === clientId).result)
  }

  def findAllInvoices: Future[Seq[DBInvoice]] = {
    val q = slickInvoice.result
    db.run(q)
//      .map { invoices =>
//      invoices.groupBy(_._1).map { x =>
//        Invoice(
//          id = x._1.id,
//          date = x._1.date,
//          number = x._1.number,
//          clientId = x._1.clientId,
//          services = x._2.flatMap(_._2)
//        )
//      }.toSeq
//    }
  }
}

