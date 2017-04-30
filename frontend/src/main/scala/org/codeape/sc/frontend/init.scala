package org.codeape.sc.frontend

import io.udash._
import io.udash.wrappers.jquery._
import org.codeape.sc.shared.MainServerREST
import org.scalajs.dom
import org.scalajs.dom.{Element, document}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scala.util.Try

object Context {
  implicit val executionContext = scalajs.concurrent.JSExecutionContext.Implicits.queue
  private val routingRegistry = new RoutingRegistryDef
  private val viewPresenterRegistry = new StatesToViewPresenterDef

  implicit val applicationInstance = new Application[RoutingState](routingRegistry, viewPresenterRegistry, RootState)

  import io.udash.rest._
  val port = Try(dom.window.location.port.toInt).getOrElse(80)
  val host = dom.window.location.hostname
  val restServer = DefaultServerREST[MainServerREST](host, port, "/api/")
}

object Init extends JSApp with StrictLogging {
  import Context._

  @JSExport
  override def main(): Unit = {
    logger.info("StampClock started")
    jQ(document).ready((_: Element) => {
      val appRoot = jQ("#application").get(0)
      if (appRoot.isEmpty) {
        logger.error("Application root element not found! Check your index.html file!")
      } else {
        applicationInstance.run(appRoot.get)
      }
    })
  }
}