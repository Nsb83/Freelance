import java.time.Clock

import javax.inject.{Inject, Provider, Singleton}
import com.google.inject.AbstractModule
import com.typesafe.config.Config
import daos.{ClientDAO, ClientDAOImpl, InvoiceDAO, InvoiceDAOImpl}
import play.api.inject.ApplicationLifecycle
import play.api.{Configuration, Environment}
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Future

import services.{ApplicationTimer, AtomicCounter, Counter}

/**
  * This module handles the bindings for the API to the Slick implementation.
  *
  * https://www.playframework.com/documentation/latest/ScalaDependencyInjection#Programmatic-bindings
  */

class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    bind(classOf[ApplicationTimer]).asEagerSingleton()
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[Counter]).to(classOf[AtomicCounter])
  }

}



//class Module(environment: Environment,
//             configuration: Configuration) extends AbstractModule {
//  override def configure(): Unit = {
//    bind(classOf[Database]).toProvider(classOf[DatabaseProvider])
//    bind(classOf[ClientDAO]).to(classOf[ClientDAOImpl])
//    bind(classOf[InvoiceDAO]).to(classOf[InvoiceDAOImpl])
//  }
//}
//
//@Singleton
//class DatabaseProvider @Inject() (config: Config) extends Provider[Database] {
//  lazy val get = Database.forConfig("freelance.database", config)
//}



