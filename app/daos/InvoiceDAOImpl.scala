package daos

import daos.tables.{InvoiceDAOTables, ServiceDAOTables}
import javax.inject.Inject
import models._
import org.joda.time.LocalDateTime
import play.api.libs.json.{JsValue, Json}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

class InvoiceDAOImpl @Inject() (override protected val dbConfigProvider: _root_.play.api.db.slick.DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends InvoiceDAO with InvoiceDAOTables with ServiceDAOTables {

  import profile.api._

  def saveInvoice(invoice: DBInvoice, services: Seq[DBService]): Future[Seq[Int]] = {
    val invoiceSave = (slickInvoice += invoice).flatMap { _ =>
      DBIO.sequence(services.map { dbServices =>
        slickService += dbServices
      })
    }
    db.run(invoiceSave)
  }

  def checkIfZero: Future[Int] = {
    db.run(slickInvoice.length.result)
  }

  def findLastNumber: Future[Seq[DBInvoice]] = {
    db.run(slickInvoice.sortBy(_.id).result)
  }

  //    def findCompleteInvoiceByClient(clientId: String): Future[Seq[Invoice]] = {
  //      val query = slickInvoice.filter(_.clientId === clientId)
  //        .joinLeft(slickService).on(_.id === _.invoiceId)
  //      db.run(query.result).map { invoices =>
  //        invoices.groupBy(_._1).map { x =>
  //          Invoice(
  //            id = x._1.id,
  //            publicId = x._1.publicId,
  //            date = x._1.date,
  //            number = x._1.number,
  //            clientId = x._1.clientId,
  //            services = x._2.flatMap(_._2)
  //          )
  //        }
  //      }
  //    }

  //  private def makeJsonService(service: DBService): JsValue = {
  //    Json.toJson(Json.obj(
  //      "id" -> service.serviceId,
  //      "invoiceId" -> service.invoiceId,
  //      "serviceName" -> service.serviceName,
  //      "quantity" -> service.quantity,
  //      "unitPrice" -> service.unitPrice,
  //      "VATRate" -> service.VATRate,
  //      "totalDutyFreePrice" -> service.totalDutyFreePrice,
  //      "VATTotal" -> service.VATTotal,
  //      "totalPrice" -> service.totalPrice
  //    ))
  //  }


  def findServices(invoiceId: InvoiceId) = {
    val q = slickService.filter(_.invoiceId === invoiceId)
    db.run(q.result).map { services =>
      services.map(s =>
        DBService(
          serviceId = s.serviceId,
          invoiceId = s.invoiceId,
          serviceName = s.serviceName,
          quantity = s.quantity,
          unitPrice = s.unitPrice,
          VATRate = s.VATRate
        ))
    }
  }


  def findCalculatedInvoice(invoiceId: InvoiceId) = {
    val calculatedInvoice = findServices(invoiceId).map { dbService =>
      dbService.map(_.toService)
    }
    calculatedInvoice
  }

  def findInvoice(invoiceId: InvoiceId): Future[immutable.Iterable[Invoice]] = {
    val query = slickInvoice.filter(_.id === invoiceId)
      .joinLeft(slickService.filter(_.invoiceId === invoiceId))
    db.run(query.result).map { SeqdbInvoiceOptDbService =>
      SeqdbInvoiceOptDbService.groupBy(_._1).map { x =>
        Invoice(
          id = x._1.id,
          publicId = x._1.publicId,
          date = x._1.date,
          number = x._1.number,
          clientId = x._1.clientId,
          services = x._2.flatMap(_._2)
        )
      }
    }
  }

  //
  def getInvoice(invoiceId: InvoiceId): Future[Seq[Invoice]] = {
    val query = slickInvoice.filter(_.id === invoiceId)
      .joinLeft(slickService.filter(_.invoiceId === invoiceId))
    db.run(query.result).map { seqInvoice =>
      seqInvoice.groupBy(_._1).map { x =>
        Invoice(
          id = x._1.id,
          publicId = x._1.publicId,
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

  def findAllInvoices: Future[Seq[Invoice]] = {
    val q = slickInvoice.joinLeft(slickService).on(_.id === _.invoiceId)
    db.run(q.result).map { seqInvoice =>
      seqInvoice.groupBy(_._1).map {x =>
        Invoice(
          id = x._1.id,
          publicId = x._1.publicId,
          date = x._1.date,
          number = x._1.number,
          clientId = x._1.clientId,
          services = x._2.flatMap(_._2)
        )
      }.toSeq
    }
  }
}

