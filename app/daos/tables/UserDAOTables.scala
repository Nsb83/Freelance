package daos.tables

import com.mohiva.play.silhouette.api.LoginInfo
import daos.DAOSlick
import models.{DBUser, User}

trait UserDAOTables extends DAOSlick {

  import profile.api._

  class UserDBTable(tag: Tag) extends Table[DBUser](tag, "User"){
    def id = column[String]("UserId", O.PrimaryKey)
    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def fullName = column[String]("fullName")
    def email = column[String]("email")
    def phoneNumber = column[String]("phoneNumber")
    def SIRENNUmber = column[String]("SIRENNumber")

    def * = (id, firstName, lastName, fullName, email, phoneNumber, SIRENNUmber) <> ((DBUser.apply _).tupled, DBUser.unapply)
  }

  case class DBLoginInfo (
                           id: Option[Long],
                           providerID: String,
                           providerKey: String)

  class LoginInfoDBTable(tag: Tag) extends Table[DBLoginInfo](tag, "Logininfo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def providerID = column[String]("providerID")
    def providerKey = column[String]("providerKey")
    def * = (id.?, providerID, providerKey) <> (DBLoginInfo.tupled, DBLoginInfo.unapply)
  }

  case class DBUserLoginInfo (
                               id: Option[Long],
                               userID: String,
                               loginInfoId: Long)


  class UserLoginInfoDBTable(tag: Tag) extends Table[DBUserLoginInfo](tag, "Userlogininfo") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def userID = column[String]("userID")
    def loginInfoId = column[Long]("loginInfoId")
    def * = (id.?, userID, loginInfoId) <> (DBUserLoginInfo.tupled, DBUserLoginInfo.unapply)
  }

  // table query definitions
  val slickUser = TableQuery[UserDBTable]
  val slickLoginInfo = TableQuery[LoginInfoDBTable]
  val slickUserLoginInfo = TableQuery[UserLoginInfoDBTable]

  // queries used in multiple places
  def loginInfoQuery(loginInfo: LoginInfo): Query[LoginInfoDBTable, DBLoginInfo, Seq] =
    slickLoginInfo.filter(dbLoginInfo => dbLoginInfo.providerID === loginInfo.providerID && dbLoginInfo.providerKey === loginInfo.providerKey)

}
