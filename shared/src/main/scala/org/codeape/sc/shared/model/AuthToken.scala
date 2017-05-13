package org.codeape.sc.shared.model

import com.avsystem.commons.serialization.GenCodec

case class AuthToken(id: String)
case class AuthTokenRequest(user: String, group: String, password: String)

object AuthToken {
  implicit val authTokenGenCodec: GenCodec[AuthToken] = GenCodec.materialize[AuthToken]
}

object AuthTokenRequest {
  implicit val authTokenRequest: GenCodec[AuthTokenRequest] = GenCodec.materialize[AuthTokenRequest]
}