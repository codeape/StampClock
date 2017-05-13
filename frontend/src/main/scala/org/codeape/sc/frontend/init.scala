package org.codeape.sc.frontend

import io.udash._
import io.udash.wrappers.jquery._
import org.codeape.sc.shared.MainServerREST
import org.scalajs.dom
import org.scalajs.dom.{Element, document}

import scala.concurrent.ExecutionContextExecutor
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

object Context {
  implicit val executionContext: ExecutionContextExecutor = scalajs.concurrent.JSExecutionContext.Implicits.queue
  private val routingRegistry: RoutingRegistryDef = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewPresenterDef

  implicit val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry, RootState)

  import io.udash.rest._
  val port: Int = Try(dom.window.location.port.toInt).getOrElse(80)
  val host: String = dom.window.location.hostname
  val restServer: MainServerREST = DefaultServerREST[MainServerREST](host, port, "/api/")

  def getToken(): String = jQ("#token").value().asInstanceOf[String]

  def setToken(tok: String)  = jQ("#token").value(tok)
}

object Init extends JSApp with StrictLogging {
  import Context._

  @JSExport
  override def main(): Unit = {
    logger.info("StampClock started")
    jQ(document).ready((_: Element) => {
      logger.info(s"Token ${Context.getToken()}")
      Context.setToken("korv")
      logger.info(s"Token ${Context.getToken()}")
      val appRoot = jQ("#application").get(0)
      if (appRoot.isEmpty) {
        logger.error("Application root element not found! Check your index.html file!")
      } else {
        applicationInstance.run(appRoot.get)
        applicationInstance.goTo(LoginFormState())
      }
    })
  }
}