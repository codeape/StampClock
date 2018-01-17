package org.codeape.sc.backend.filters

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.StatusCodes.Unauthorized
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

class AuthFilter(system: ActorSystem) {

  val log: LoggingAdapter = Logging.getLogger(system, this)
  log.info("Created Auth filter")

  def route: Route = pathPrefix("") {
    optionalHeaderValueByName("stamptoken") { headerVal =>
      log.debug(s"Received token \n")
      headerVal match {
        case Some(token) => if (token == "pw") reject else complete(Unauthorized)
        case _ => complete(Unauthorized)
      }
    }
  }

}
