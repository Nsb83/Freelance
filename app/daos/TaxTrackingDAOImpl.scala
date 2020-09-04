package daos

import com.google.inject.Inject
import daos.tables.TaxTrackingDAOTables
import models.{DBIncomeTaxTracking, DBTaxTracking, InvoiceId}
import play.api.Logger
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class TaxTrackingDAOImpl @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit val ex: ExecutionContext) extends TaxTrackingDAO with TaxTrackingDAOTables {

  import profile.api._

  def save(taxTracking: DBTaxTracking, invoicesId: Seq[InvoiceId]): Future[Int] = {
    val actions = ((slickTaxTracking returning slickTaxTracking.map(_.id)) += taxTracking).flatMap { newTaxId =>
      DBIO.fold(invoicesId.map { invoiceId =>
        slickIncomeTaxTracking += DBIncomeTaxTracking(newTaxId, invoiceId)
      }, 0)(_ + _) // TODO Ca marche ça ?
    }
    db.run(actions.asTry).map {
      case Success(value) => value
      case Failure(ex) => Logger.debug(ex.getMessage)
        0
    }
  }

}
