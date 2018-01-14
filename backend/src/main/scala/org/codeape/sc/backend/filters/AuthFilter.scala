package org.codeape.sc.backend.filters

import akka.http.scaladsl.model.StatusCodes.Unauthorized
import akka.http.scaladsl.server.{Directive, Route}
import akka.http.scaladsl.server.Directives._

class AuthFilter {

  val allMethods: Directive[Unit] = delete | get | head | options | patch | post | put

  def route: Route = pathPrefix("") {
    (allMethods & optionalHeaderValueByName("stamptoken")) { headerVal =>
      print(s"pw = $headerVal \n")
      headerVal match {
        case Some(token) => if (token == "pw") reject else complete(Unauthorized)
        case _ => complete(Unauthorized)
      }
    }
  }

}
