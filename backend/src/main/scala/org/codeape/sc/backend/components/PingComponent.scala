package org.codeape.sc.backend.components

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.StampClockJsonMarshaller
import org.codeape.sc.shared.model.{PingReply, PingRequest}

class PingComponent extends StampClockJsonMarshaller {

  def route: Route = path("util" / "ping") {
    post {
      entity(as[PingRequest]) { ping =>
        complete { PingReply(msg = s"You said ${ping.msg} I say pong!") }
      }
    }
  }
}
