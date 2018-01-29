package org.codeape.sc.backend.jwt

import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import scala.util.{Failure, Success, Try}

class JwtCodecHS256(secret: String) extends JsonWebToken {

  import JwtMarshaller._
  import spray.json._

  override val headerJson: String = Header(alg = "HS256", typ = "JWT").toJson.compactPrint

  override val minKeyBitSize: Int = 256

  override def keyBitSize: Int = secret.getBytes(StandardCharsets.UTF_8).length * 8

  private def headerBase64: String = new String(Base64.getEncoder.encode(headerJson.getBytes(StandardCharsets.UTF_8)))

  private def hash(msg: String): String = {
    val sha256_HMAC = Mac.getInstance("HmacSHA256")
    val secret_key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")
    sha256_HMAC.init(secret_key)
    new String(Base64.getEncoder.encode(sha256_HMAC.doFinal(msg.getBytes(StandardCharsets.UTF_8))))
  }

  override def encode(payload: Payload): JwtEncodeStatus = {
    val payloadJson = payload.toJson.compactPrint
    val payloadBase64 = new String(Base64.getEncoder.encode(payloadJson.getBytes(StandardCharsets.UTF_8)))
    val msg = headerBase64 + "." + payloadBase64
    Try {
      msg + "." + hash(msg)
    } match {
      case Success(v: String) => EncodeSuccess(v)
      case Failure(e) => EncodeError(e)
    }
  }

  override def decode(token: String): JwtDecodeStatus = {
    val tokenParts = token.split('.')
    if (tokenParts.length == 3) {
      val msg = tokenParts(0) + "." + tokenParts(1)
      Try{
        val createdToken = hash(msg)
        if (createdToken == tokenParts(2)) {
          val decodedPayload = new String(Base64.getDecoder.decode(tokenParts(1)))
          val parsedJson = decodedPayload.parseJson
          val convertedPayload = parsedJson.convertTo[Payload]
          DecodeSuccess(convertedPayload)
        } else {
          DecodeAuthFailed(s"Hash comparison did not match $token")
        }
      } match {
        case Success(v) => v
        case Failure(e) => DecodeError(e)
      }
    } else {
      DecodeAuthFailed(s"Wrong number of token parts: ${tokenParts.length}")
    }
  }

}
