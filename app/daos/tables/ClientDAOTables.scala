package daos.tables

import daos.DAOSlick
import models.{DBClient, UserID}

trait ClientDAOTables extends DAOSlick {

  import profile.api._

  class ClientDBTable(tag: Tag) extends Table[DBClient](tag, "Client") {
    def id = column[String]("id", O.PrimaryKey)
    def companyName = column[String]("companyName")
    def referentFirstName = column[String]("referentFirstName")
    def referentLastName = column[String]("referentLastName")
    def address  = column[String]("address")
    def postalCode = column[String]("postalCode")
    def city = column[String]("city")
    def email = column[String]("email")
    def phoneNumber = column[String]("phoneNumber")
    def VATNumber = column[String]("VATNumber")
    def isActive = column[Boolean]("isActive")
    def userId = column[UserID]("userId")
    def * = (
      id,
      companyName,
      referentFirstName,
      referentLastName,
      address,
      postalCode,
      city,
      email,
      phoneNumber,
      VATNumber,
      isActive,
      userId) <> ((DBClient.apply _).tupled, DBClient.unapply)


    def toUpdate = (
      id,
      companyName,
      referentFirstName,
      referentLastName,
      address,
      postalCode,
      city,
      email,
      phoneNumber,
      VATNumber,
      isActive,
      userId) <> ((DBClient.apply _).tupled, DBClient.unapply)
  }

  lazy val slickClient = TableQuery[ClientDBTable]

}
