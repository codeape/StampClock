package org.codeape.sc.backend.components


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.StampClockJsonMarshaller
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}

class LoginComponent extends StampClockJsonMarshaller {

  def route: Route = pathPrefix("auth") {
    path("login") {
      post {
        entity(as[AuthTokenRequest]) { request =>
          complete { AuthToken(id = request.password) }
        }
      }
    }
  }

}
