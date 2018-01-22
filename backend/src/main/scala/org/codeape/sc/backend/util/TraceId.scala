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

}

trait TraceIdLogger {

  val log: LoggingAdapter

  def debug(traceId: Option[TraceId], msg: String): Unit = {
    log.debug("[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") + "]"  + " " + msg)
  }

  def info(traceId: Option[TraceId], msg: String): Unit = {
    log.isDebugEnabled match {
      case true => log.info("[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") +
        "]"  + " " + msg)
      case false => log.info(msg)
    }
  }

  def warning(traceId: Option[TraceId], msg: String): Unit = {
    log.isDebugEnabled match {
      case true => log.warning("[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") +
        "]"  + " " + msg)
      case false => log.warning(msg)
    }
  }

  def error(traceId: Option[TraceId], msg: String): Unit = {
    log.isDebugEnabled match {
      case true => log.error("[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") +
        "]"  + " " + msg)
      case false => log.error(msg)
    }
  }

  def error(traceId: Option[TraceId], throwable: Throwable, msg: String): Unit = {
    log.isDebugEnabled match {
      case true => log.error(throwable,
        "[" + traceId.map(trace => s"TraceId: ${trace.id}").getOrElse("TraceId: NA") + "]"  + " " + msg)
      case false => log.error(throwable, msg)
    }
  }

}