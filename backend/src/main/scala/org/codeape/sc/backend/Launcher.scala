package org.codeape.sc.backend

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import org.codeape.sc.backend.components.{LoginComponent, PingComponent}
import org.codeape.sc.backend.filters.AuthFilter
import org.codeape.sc.backend.jwt.{JsonWebToken, JwtCodecHS256}

import scala.concurrent.ExecutionContextExecutor
import scala.io.StdIn

object Launcher {

  def main(args: Array[String]): Unit = {
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val log = Logging.getLogger(system, this)

    val jwt: JsonWebToken = new JwtCodecHS256("test")
    log.info(s"JWT key length: ${jwt.keyBitSize} bit")
    if(jwt.keyBitSize != jwt.minKeyBitSize) {
      log.warning(s"JWT key length not ${jwt.minKeyBitSize} bit")
    }
    log.debug(s"JWT header: ${jwt.headerJson}")

    val loginComponent = new LoginComponent(system, jwt)
    val authFilter = new AuthFilter(system)
    val pingComponent = new PingComponent(system, jwt)

    val route =
      (path("") & get) {
        getFromFile("backend/target/UdashStatic/WebContent/index.html")
      } ~
      (pathPrefix("scripts") & get){
        getFromDirectory("backend/target/UdashStatic/WebContent/scripts")
      } ~
      (pathPrefix("assets")  & get){
        getFromDirectory("backend/target/UdashStatic/WebContent/assets")
      } ~
      pathPrefix("api") {
        loginComponent.route ~
        // Add all components/filters that do not use auth above
        authFilter.route ~
        // Add all components/filters that require auth below
        pingComponent.route
      }


    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    log.info("Server online at http://localhost:8080/")
    log.info("Press RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}

       
