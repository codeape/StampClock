package org.codeape.sc.backend.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest}
import spray.json.DefaultJsonProtocol

trait StampClockJsonMarshaller extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val formatAuthToken = jsonFormat1(AuthToken)
  implicit val formatAuthTokenRequest = jsonFormat3(AuthTokenRequest)
}
