import scala.sys.process.Process
import sbt._
import play.sbt.PlayRunHook
import com.typesafe.config._

import java.net.InetSocketAddress

object WebpackServer {
  def apply(base: File): PlayRunHook = {
    object WebpackServerScript extends PlayRunHook {
      var process: Option[scala.sys.process.Process] = None
      val config: Config = ConfigFactory.parseFile(new File("conf/frontend.conf")).resolve()
      val isWin: Boolean = System.getProperty("os.name").toUpperCase().indexOf("WIN") >= 0
      override def afterStarted(): Unit = {
        val port = config.getInt("webpack.port")
        process = if (isWin)
          Option(Process(s"cmd /c npm run watch -- --port $port", base).run)
        else
          Option(Process(s"npm run watch -- --port $port", base).run)
      }
      override def afterStopped(): Unit = {
        process.foreach(p => { p.destroy() })
        process = None
      }
    }

    WebpackServerScript
  }
}