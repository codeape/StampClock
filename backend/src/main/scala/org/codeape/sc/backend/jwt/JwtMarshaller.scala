package org.codeape.sc.backend.jwt

import spray.json.{DefaultJsonProtocol, RootJsonFormat}

sealed trait JwtComponent

case class Header(
  alg: String,
  typ: String
) extends JwtComponent

case class Payload(
  sub: String, // Identifies the subject of the JWT
  iat: Long, // The "issued at" claim identifies the time at which the JWT was issued.
  role: String, // The role this token gives access to
  org: String // The organisation this token gives access to
) extends JwtComponent

object JwtMarshaller extends DefaultJsonProtocol {

  implicit val formatHeader: RootJsonFormat[Header] = jsonFormat2(Header.apply)
  implicit val formatPayload: RootJsonFormat[Payload] = jsonFormat4(Payload.apply)

}
