package models

import ai.x.play.json.Jsonx
import play.api.libs.json.{Format, Json, OFormat}
import play.api.mvc.PathBindable
import slick.lifted.MappedTo

case class DBBank (id: BankID = BankID(0),
                   bankName: String,
                   BICNumber: String,
                   IBANNumber: String,
                   userId: UserID)

case class BankID(value: Long) extends AnyVal with MappedTo[Long]

object BankID {
  implicit val formatter: Format[BankID] = Jsonx.formatAuto[BankID]
  implicit def pathBinder(implicit longBinder: PathBindable[Long]): PathBindable[BankID] {
    def bind(key: String, value: String): Either[String, BankID]

    def unbind(key: String, value: BankID): String
  } = new PathBindable[BankID] {
    override def bind(key: String, value: String): Either[String, BankID] = {
      for {
        id <- longBinder.bind(key, value).right
      } yield {
        BankID(id)
      }
    }
    override def unbind(key: String, value: BankID): String = value.value.toString
  }
}

object DBBank {
  implicit val formatter: OFormat[DBBank] = Json.format[DBBank]
}

