package org.codeape.sc.backend.components


import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.{StampClockJsonMarshaller, TraceIdLogger}
import org.codeape.sc.backend.util.TraceId.trace
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}

class LoginComponent(system: ActorSystem) extends StampClockJsonMarshaller with TraceIdLogger {

  override val log: LoggingAdapter = Logging.getLogger(system, this)
  log.info("Created Login component")

  def route: Route = pathPrefix("auth") {
    path("login"){
      post {
        (entity(as[AuthTokenRequest]) & trace) { (auth, traceId)=>
          debug(traceId, s"Received login request")
          complete { AuthToken(id = auth.password) }
        }
      }
    }
  }

}
