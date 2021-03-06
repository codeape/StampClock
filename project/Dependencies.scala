import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies {
  val udashVersion = "0.5.0"
  val udashJQueryVersion = "1.0.1"

  val akkaVersion = "2.5.8"
  val akkaHttpVersion = "10.1.0-RC1"
  val logbackVersion = "1.3.0-alpha4"

  val scalaTestVersion = "3.0.4"
  val jodaTimeVersion = "2.18.0"
  val phantomVersion = "2.23.0"
  val configsVersion = "0.4.4"

  val crossDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-shared" % udashVersion,
    "io.udash" %%% "udash-rest-shared" % udashVersion
  ))

  val frontendDeps = Def.setting(Seq[ModuleID](
    "io.udash" %%% "udash-core-frontend" % udashVersion,
    "io.udash" %%% "udash-bootstrap" % udashVersion,
    "io.udash" %%% "udash-jquery" % udashJQueryVersion
  ))

  val frontendJSDeps = Def.setting(Seq[org.scalajs.sbtplugin.JSModuleID](
  ))

  val backendDeps = Def.setting(Seq[ModuleID](
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "org.scalactic" %% "scalactic" % scalaTestVersion,
    "com.outworkers"  %% "phantom-dsl" % phantomVersion,
    "com.github.kxbmap" %% "configs" % configsVersion,
    "org.scalatest" %% "scalatest" % scalaTestVersion % "test",
    "com.github.nscala-time" %% "nscala-time" % jodaTimeVersion % "test"
  ))
}
