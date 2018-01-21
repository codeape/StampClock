package org.codeape.sc.frontend.views.util

object TraceId {

  def getTraceId(): String = scala.util.Random.alphanumeric.take(8).mkString

}
