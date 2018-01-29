package org.codeape.sc.backend.jwt

import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.{FlatSpec, Inside, Matchers}

class JwtCodecHS256Test extends FlatSpec with Inside with Matchers {

  val jwtCodecHS256 = new JwtCodecHS256("GRFMUCHkxJ4i8n2UAJ8jGf9hMdNJLqNz")

  "A JwtCodecHS256" should "be able to create a token and validate it" in {
    jwtCodecHS256.keyBitSize shouldBe jwtCodecHS256.minKeyBitSize

    val payload = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis,
      org="myorg",
      role="user"
    )
    val token = jwtCodecHS256.encode(payload) match {
      case EncodeSuccess(v) => v
      case EncodeError(e) => fail(s"Encode returned EncodeError: $e")
    }
    val decoded = jwtCodecHS256.decode(token) match {
      case DecodeSuccess(v) => v
      case DecodeError(e) => fail(s"Encode returned DecodeError: $e")
      case DecodeAuthFailed(e) => fail(s"Encode returned DecodeAuthFailed: $e")
    }

    payload.sub shouldBe decoded.sub
    payload.iat shouldBe decoded.iat
    payload.org shouldBe decoded.org
    payload.role shouldBe decoded.role
  }

  it should "fail when not all parts of the token is there" in {
    val payload = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis,
      org="myorg",
      role="user"
    )
    val token = jwtCodecHS256.encode(payload) match {
      case EncodeSuccess(v) => v
      case EncodeError(e) => fail(s"Encode returned EncodeError: $e")
    }
    val splitToken = token.split('.')
    val decoded = jwtCodecHS256.decode(splitToken(0) + "." + splitToken(1))
    inside(decoded) {
      case DecodeAuthFailed(msg) => msg shouldBe "Wrong number of token parts: 2"
    }
  }

  it should "fail when the check sums do not add up" in {
    val payload1 = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis,
      org="myorg",
      role="user"
    )
    val token1 = jwtCodecHS256.encode(payload1) match {
      case EncodeSuccess(v) => v
      case EncodeError(e) => fail(s"Encode returned EncodeError: $e")
    }

    val payload2 = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis + 1000,
      org="myorg",
      role="user"
    )
    val token2 = jwtCodecHS256.encode(payload2) match {
      case EncodeSuccess(v) => v
      case EncodeError(e) => fail(s"Encode returned EncodeError: $e")
    }

    val splitToken1 = token1.split('.')
    val splitToken2 = token2.split('.')
    val decoded = jwtCodecHS256.decode(splitToken1(0) + "." + splitToken1(1) + "." + splitToken2(2))
    inside(decoded) {
      case DecodeAuthFailed(msg) => msg startsWith "Hash comparison did not match"
    }
  }

  it should "fail with EncodeError on Exception when encoding" in {
    val failJwt = new JwtCodecHS256("")
    val payload = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis,
      org="myorg",
      role="user"
    )
    failJwt.encode(payload) match {
      case EncodeSuccess(v) => fail(s"Encode returned EncodeSuccess: $v")
      case EncodeError(e) => e.isInstanceOf[IllegalArgumentException] shouldBe true
    }
  }


  it should "fail with DecodeError on Exception when decoding" in {
    val failJwt = new JwtCodecHS256("")
    val payload = Payload(
      sub="testuser",
      iat=DateTime.now(DateTimeZone.UTC).getMillis,
      org="myorg",
      role="user"
    )
    val token = jwtCodecHS256.encode(payload) match {
      case EncodeSuccess(v) => v
      case EncodeError(e) => fail(s"Encode returned EncodeError: $e")
    }
    val decoded = failJwt.decode(token)
    inside(decoded){
      case DecodeError(e) => e.isInstanceOf[IllegalArgumentException] shouldBe true
    }
  }

}
