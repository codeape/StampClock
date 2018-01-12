package org.codeape.sc.backend.components


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.codeape.sc.backend.util.StampClockJsonMarshaller
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}

class LoginComponent extends StampClockJsonMarshaller {

  private val authToken = AuthToken(id = "Testreply")

  val route: Route = pathPrefix("auth") {
    pathPrefix("login") {
      post {
        entity(as[AuthTokenRequest]) { request =>
          complete { authToken }
        }
      }
    }
  }

}
