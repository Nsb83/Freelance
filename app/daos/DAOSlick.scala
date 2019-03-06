package daos

import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

trait DAOSlick extends HasDatabaseConfigProvider[JdbcProfile]
