package org.codeape.sc.backend.components

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.jwt.JsonWebToken
import org.codeape.sc.backend.util.{StampClockJsonMarshaller, TraceIdLogger}
import org.codeape.sc.backend.util.TraceId.trace
import org.codeape.sc.shared.model.{PingReply, PingRequest}

class PingComponent(system: ActorSystem, jwt: JsonWebToken) extends StampClockJsonMarshaller with TraceIdLogger {

  override val log: LoggingAdapter = Logging.getLogger(system, this)
  log.info("Created Ping component")

  def route: Route = path("util" / "ping") {
    post {
      (entity(as[PingRequest]) & trace) { (ping, traceId) =>
        debug(traceId, "Received ping request")
        complete { PingReply(msg = s"You said ${ping.msg} I say pong!") }
      }
    }
  }
}
