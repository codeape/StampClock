package org.codeape.sc.shared.model

import com.avsystem.commons.serialization.GenCodec

case class PingRequest(msg: String)

case class PingReply(msg: String)

object PingRequest extends (String => PingRequest) {
  implicit val pingRequestGenCodec: GenCodec[PingRequest] = GenCodec.materialize[PingRequest]
}

object PingReply extends (String => PingReply) {
  implicit val pingReplyGenCodec: GenCodec[PingReply] = GenCodec.materialize[PingReply]
}