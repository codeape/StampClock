package org.codeape.sc.backend.components


import org.codeape.sc.backend.util.StampClockMarshaller._
import akka.http.scaladsl.server.Directives._
import io.udash.rpc.DefaultUdashSerialization
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}

class LoginComponent extends DefaultUdashSerialization {

  private val authToken = AuthToken(id = "Testreply")

  val route = pathPrefix("auth") {
    pathPrefix("login") {
      post {
        entity(as[AuthTokenRequest]) { request =>
          complete { authToken }
        }
      }
    }
  }

}
