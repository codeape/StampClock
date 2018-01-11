package org.codeape.sc.shared.model

import com.avsystem.commons.serialization.GenCodec

case class AuthToken(id: String)
case class AuthTokenRequest(user: String, group: String, password: String)

// Because jsonFormat, explicit companions need to conform to FunctionN, like compiler generated companions.
object AuthToken extends ((String) => AuthToken) {
  implicit val authTokenGenCodec: GenCodec[AuthToken] = GenCodec.materialize[AuthToken]
}

object AuthTokenRequest extends ((String, String, String) => AuthTokenRequest){
  implicit val authTokenRequest: GenCodec[AuthTokenRequest] = GenCodec.materialize[AuthTokenRequest]
}