package daos

import daos.tables.{BankDAOTables, ClientDAOTables, InvoiceDAOTables, ServiceDAOTables, UserDAOTables}
import javax.inject.Inject
import models._
import org.joda.time.LocalDateTime
import play.api.db.slick.DatabaseConfigProvider
import play.api.libs.json.{JsValue, Json}

import scala.collection.immutable
import scala.concurrent.{ExecutionContext, Future}

class InvoiceDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit val executionContext: ExecutionContext)
  extends InvoiceDAO with InvoiceDAOTables with ServiceDAOTables with ClientDAOTables with UserDAOTables with BankDAOTables {

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

  def findLastNumberForUser(userID: UserID): Future[Seq[DBInvoice]] = {
    db.run(slickInvoice.filter(_.userId === userID).sortBy(_.id).result)
  }

  def findLastNumber: Future[Seq[DBInvoice]] = {
    db.run(slickInvoice.sortBy(_.id).result)
  }


  def findServices(invoiceId: InvoiceId): Future[Seq[DBService]] = {
    val q = slickService.filter(_.invoiceId === invoiceId)
    db.run(q.result).map { services =>
      services.map(s =>
        DBService(
          serviceId = s.serviceId,
          invoiceId = s.invoiceId,
          serviceNumber = s.serviceNumber,
          serviceName = s.serviceName,
          quantity = s.quantity,
          unitPrice = s.unitPrice,
          VATRate = s.VATRate
        ))
    }
  }


  def findCalculatedInvoice(invoiceId: InvoiceId): Future[Seq[Service]] = {
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
          period = x._1.period,
          number = x._1.number,
          clientId = x._1.clientId,
          services = x._2.flatMap(_._2),
          userID = x._1.userID
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
          period = x._1.period,
          number = x._1.number,
          clientId = x._1.clientId,
          services = x._2.flatMap(_._2),
          userID = x._1.userID
        )
      }.toSeq
    }
  }


  def findInvoiceByClient(clientId: String): Future[Seq[DBInvoice]] = {
    db.run(slickInvoice.filter(_.clientId === clientId).result)
  }

  def findAllInvoicesWithClient(userID: UserID): Future[Seq[InvoiceWithClient]] = {
    val q = slickInvoice
      .filter(_.userId === userID)
      .joinLeft(slickService).on(_.id === _.invoiceId)
      .join(slickClient).on(_._1.clientId === _.id)
    db.run(q.result).map { seqInvoice =>
      seqInvoice.groupBy(_._1._1).map { tuple =>
        val dbInvoice: DBInvoice = tuple._1
        val services: Seq[DBService] = tuple._2.flatMap(_._1._2)
        val client = tuple._2.map(_._2).head

        InvoiceWithClient(
          id = dbInvoice.id,
          publicId = dbInvoice.publicId,
          date = dbInvoice.date,
          period = dbInvoice.period,
          number = dbInvoice.number,
          client = client,
          services = services,
          userID = dbInvoice.userID
        )
      }.toSeq
    }
  }
  def findCompleteInvoice(publicId: String): Future[Seq[InvoiceWithClient]] = {
    val query = slickInvoice.filter(_.publicId === publicId)
      .joinLeft(slickService).on(_.id === _.invoiceId)
      .join(slickClient).on(_._1.clientId === _.id)

    db.run(query.result).map { result =>
      result.groupBy(_._1._1).map { tuple =>
        val dbInvoice: DBInvoice = tuple._1
        val services: Seq[DBService] = tuple._2.flatMap(_._1._2)
        val client = tuple._2.map(_._2).head

        InvoiceWithClient(
          id = dbInvoice.id,
          publicId = dbInvoice.publicId,
          date = dbInvoice.date,
          period = dbInvoice.period,
          number = dbInvoice.number,
          client = client,
          services = services,
          userID = dbInvoice.userID
        )
      }.toSeq
      }
    }
}

