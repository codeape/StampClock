package org.codeape.sc.shared

import org.codeape.sc.shared.model._
import io.udash.rest._

import scala.concurrent.Future

@REST
trait MainServerREST {

  def auth(): AuthREST

}

@REST
trait AuthREST {

  @POST
  def login(@Body request: AuthTokenRequest): Future[AuthToken]

}