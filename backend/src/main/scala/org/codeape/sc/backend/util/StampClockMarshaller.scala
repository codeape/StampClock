package org.codeape.sc.backend.util

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.{HttpEntity, MediaTypes, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.RequestContext
import io.udash.rpc.DefaultUdashSerialization
import akka.http.scaladsl.unmarshalling.{FromEntityUnmarshaller, Unmarshaller}
import com.avsystem.commons.serialization.GenCodec

object StampClockMarshaller extends DefaultUdashSerialization {

  implicit def optionMarshaller[T](implicit codec: GenCodec[T]): ToResponseMarshaller[Option[T]] =
    gencodecMarshaller[Option[T]](GenCodec.optionCodec(codec))

  implicit def gencodecMarshaller[T](implicit codec: GenCodec[T]): ToEntityMarshaller[T] =
    Marshaller.withFixedContentType(MediaTypes.`application/json`) { value =>
      var string: String = null
      val output = outputSerialization((serialized) => string = serialized)
      codec.write(output, value)
      HttpEntity(MediaTypes.`application/json`, string)
    }

  implicit def gencodecUnmarshaller[T](implicit codec: GenCodec[T]): FromEntityUnmarshaller[T] =
    Unmarshaller.stringUnmarshaller.forContentTypes(MediaTypes.`application/json`).map{ data =>
      val input = inputSerialization(data)
      val out: T = codec.read(input)
      out
    }

  def completeIfNonEmpty[T](ctx: RequestContext)(opt: Option[T])(implicit rm: ToResponseMarshaller[T]) =
    opt match {
      case Some(v) => complete(v)(ctx)
      case None => complete(StatusCodes.NotFound)(ctx)
    }

}
