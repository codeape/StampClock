package org.codeape.sc.backend

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object Launcher {

  def main(args: Array[String]): Unit = {
    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val route =
      path("") {
        getFromFile("backend/target/UdashStatic/WebContent/index.html")
      } ~
      pathPrefix("scripts"){
        getFromDirectory("backend/target/UdashStatic/WebContent/scripts")
      } ~
      pathPrefix("assets"){
        getFromDirectory("backend/target/UdashStatic/WebContent/assets")
      }
    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
}

       
