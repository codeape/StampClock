package org.codeape.sc.backend.jwt

sealed trait JwtEncodeStatus
case class EncodeSuccess(token: String) extends JwtEncodeStatus
case class EncodeError(e: Throwable) extends JwtEncodeStatus

sealed trait JwtDecodeStatus
case class DecodeSuccess(payload: Payload) extends JwtDecodeStatus
case class DecodeError(e: Throwable) extends JwtDecodeStatus
case class DecodeAuthFailed(msg: String) extends JwtDecodeStatus

trait JsonWebToken {

  val headerJson: String

  val minKeyBitSize: Int

  def keyBitSize: Int

  def encode(payload: Payload): JwtEncodeStatus

  def decode(token: String): JwtDecodeStatus
}
