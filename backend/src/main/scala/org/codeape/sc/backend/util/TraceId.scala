package org.codeape.sc.backend.util

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.HttpHeader
import akka.http.scaladsl.server.Directive1
import akka.http.scaladsl.server.Directives.optionalHeaderValue

case class TraceId(id: String)

object TraceId {

  private def extractTraceId : HttpHeader => Option[TraceId] = {
    case HttpHeader("traceid", id) => Some(TraceId(id))
    case _ => None
  }

  def trace: Directive1[Option[TraceId]] = optionalHeaderValue(extractTraceId)

  def debug(log: LoggingAdapter, traceId: Option[TraceId], msg: String) = {
    log.debug("[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") + "]"  + " " + msg)
  }

}
