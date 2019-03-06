package daos.tables

import daos.DAOSlick
import models.DBClient

trait ClientDAOTables extends DAOSlick {

  import profile.api._

  class ClientDBTable(tag: Tag) extends Table[DBClient](tag, "Client") {
    def id = column[String]("id", O.PrimaryKey)
    def companyName = column[String]("companyName")
    def referentFirstName = column[String]("referentFirstName")
    def referentLastName = column[String]("referentLastName")
    def email = column[String]("email")
    def phoneNumber = column[String]("phoneNumber")
    def isActive = column[Boolean]("isActive")
    def * = (
      id,
      companyName,
      referentFirstName,
      referentLastName,
      email,
      phoneNumber,
      isActive) <> ((DBClient.apply _).tupled, DBClient.unapply)


    def toUpdate = (
      id,
      companyName,
      referentFirstName,
      referentLastName,
      email,
      phoneNumber,
      isActive
    ) <> ((DBClient.apply _).tupled, DBClient.unapply)
  }

  lazy val slickClient = TableQuery[ClientDBTable]

}
