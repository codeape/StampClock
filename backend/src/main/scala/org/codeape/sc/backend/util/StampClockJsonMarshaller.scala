package org.codeape.sc.backend.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait StampClockJsonMarshaller extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val formatAuthToken: RootJsonFormat[AuthToken] = jsonFormat1(AuthToken)
  implicit val formatAuthTokenRequest: RootJsonFormat[AuthTokenRequest] = jsonFormat3(AuthTokenRequest)
}
