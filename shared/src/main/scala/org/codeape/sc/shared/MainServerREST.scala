package org.codeape.sc.shared

import org.codeape.sc.shared.model._
import io.udash.rest._

import scala.concurrent.Future

@REST
trait MainServerREST {

  def auth(): AuthREST
  def util(): UtilREST

}

@REST
trait AuthREST {

  @POST
  def login(@Header traceid: String, @Body request: AuthTokenRequest): Future[AuthToken]

}

@REST
trait UtilREST {

  @POST
  def ping(@Header traceid: String, @Header stamptoken: String, @Body request: PingRequest): Future[PingReply]

}