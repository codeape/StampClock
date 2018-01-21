package org.codeape.sc.backend.components


import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.StampClockJsonMarshaller
import org.codeape.sc.backend.util.TraceId._
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}

class LoginComponent(system: ActorSystem) extends StampClockJsonMarshaller {

  val log: LoggingAdapter = Logging.getLogger(system, this)
  log.info("Created Login component")

  def route: Route = pathPrefix("auth") {
    path("login"){
      post {
        (entity(as[AuthTokenRequest]) & trace) { (auth, traceId)=>
          debug(log, traceId, s"Received login request")
          complete { AuthToken(id = auth.password) }
        }
      }
    }
  }

}
