package daos

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import daos.tables.{PasswordInfoDAOTables, UserDAOTables}
import javax.inject.Inject
import models.{DBUser, User, UserID, UserToUpdate}
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.{ExecutionContext, Future}


/**
  * Give access to the user object using Slick
  */
class UserDAOImpl @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)(implicit ex: ExecutionContext)
  extends UserDAO with UserDAOTables with PasswordInfoDAOTables {

  import profile.api._

  /**
    * Finds a user by its login info.
    *
    * @param loginInfo The login info of the user to find.
    * @return The found user or None if no user for the given login info could be found.
    */
  def find(loginInfo: LoginInfo): Future[Option[User]] = {
    val userQuery = for {
      dbLoginInfo <- loginInfoQuery(loginInfo)
      dbUserLoginInfo <- slickUserLoginInfo.filter(_.loginInfoId === dbLoginInfo.id)
      dbUser <- slickUser.filter(_.id === dbUserLoginInfo.userID)
    } yield dbUser
    db.run(userQuery.result.headOption).map { dbUserOption =>
      dbUserOption.map { user =>
        User(user.userID, loginInfo, user.firstName, user.lastName, user.fullName, user.email, user.phoneNumber, user.address, user.postalCode, user.city, user.SIRETNumber)
      }
    }
  }


  /**
    * Finds a user by its user ID.
    *
    * @param userID The ID of the user to find.
    * @return The found user or None if no user for the given ID could be found.
    */
  def find(userID: UserID): Future[Option[User]] = {
    val query = for {
      dbUser <- slickUser.filter(_.id === userID)
      dbUserLoginInfo <- slickUserLoginInfo.filter(_.userID === dbUser.id)
      dbLoginInfo <- slickLoginInfo.filter(_.id === dbUserLoginInfo.loginInfoId)
    } yield (dbUser, dbLoginInfo)
    db.run(query.result.headOption).map { resultOption =>
      resultOption.map {
        case (user, loginInfo) =>
          User(
            user.userID,
            LoginInfo(loginInfo.providerID, loginInfo.providerKey),
            user.firstName,
            user.lastName,
            user.fullName,
            user.email,
            user.phoneNumber,
            user.address,
            user.postalCode,
            user.city,
            user.SIRETNumber)
      }
    }
  }

  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user.
    */
  def save(user: User): Future[User] = {
    val dbUser = DBUser(user.userID, user.firstName, user.lastName, user.fullName, user.email, user.phoneNumber, user.address, user.postalCode, user.city, user.SIRETNumber)
    val dbLoginInfo = DBLoginInfo(None, user.loginInfo.providerID, user.loginInfo.providerKey)
    // We don't have the LoginInfo id so we try to get it first.
    // If there is no LoginInfo yet for this user we retrieve the id on insertion.
    val loginInfoAction = {
      val retrieveLoginInfo = slickLoginInfo.filter(
        info => info.providerID === user.loginInfo.providerID &&
          info.providerKey === user.loginInfo.providerKey).result.headOption
      val insertLoginInfo = slickLoginInfo.returning(slickLoginInfo.map(_.id)).
        into((info, id) => info.copy(id = Some(id))) += dbLoginInfo
      for {
        loginInfoOption <- retrieveLoginInfo
        loginInfo <- loginInfoOption.map(DBIO.successful(_)).getOrElse(insertLoginInfo)
      } yield loginInfo
    }
    // combine database actions to be run sequentially
    val actions = (for {
      _ <- slickUser.insertOrUpdate(dbUser)
      loginInfo <- loginInfoAction
      _ <- slickUserLoginInfo += DBUserLoginInfo(None, dbUser.userID, loginInfo.id.get)
    } yield ()).transactionally
    // run actions and return user afterwards
    db.run(actions).map(_ => user)
  }

  def update(userToUpdate: UserToUpdate): Future[Int] = {
    db.run(slickUser.filter(_.id === userToUpdate.user.userID).update(userToUpdate.user))
  }

}
