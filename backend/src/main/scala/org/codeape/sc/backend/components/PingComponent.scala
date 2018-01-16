package org.codeape.sc.backend.components

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.StampClockJsonMarshaller
import org.codeape.sc.shared.model.{PingReply, PingRequest}

class PingComponent(system: ActorSystem) extends StampClockJsonMarshaller {

  val log: LoggingAdapter = Logging.getLogger(system, this)
  log.info("Created Ping component")

  def route: Route = path("util" / "ping") {
    post {
      entity(as[PingRequest]) { ping =>
        log.debug("Received ping request")
        complete { PingReply(msg = s"You said ${ping.msg} I say pong!") }
      }
    }
  }
}
