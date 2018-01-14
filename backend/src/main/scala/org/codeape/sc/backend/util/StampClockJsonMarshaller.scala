package org.codeape.sc.backend.util

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import org.codeape.sc.shared.model.{AuthToken, AuthTokenRequest, PingReply, PingRequest}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait StampClockJsonMarshaller extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val formatAuthTokenRequest: RootJsonFormat[AuthTokenRequest] = jsonFormat3(AuthTokenRequest)
  implicit val formatAuthToken: RootJsonFormat[AuthToken] = jsonFormat1(AuthToken)

  implicit val formatPingRequest: RootJsonFormat[PingRequest] = jsonFormat1(PingRequest)
  implicit val formatPingResponse: RootJsonFormat[PingReply] = jsonFormat1(PingReply)
}
