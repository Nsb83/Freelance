package daos

import models.{DBTaxTracking, InvoiceId}

import scala.concurrent.Future

trait TaxTrackingDAO {
  def save(taxTracking: DBTaxTracking, invoicesId: Seq[InvoiceId]): Future[Int]

}
