import Dependencies._
import UdashBuild._

name := "stamp-clock"

version in ThisBuild := "0.0.1"
scalaVersion in ThisBuild := "2.12.4"
organization in ThisBuild := "org.codeape"
crossPaths in ThisBuild := false
scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:implicitConversions",
  "-language:existentials",
  "-language:dynamics",
  "-Xfuture",
  "-Xfatal-warnings", //Can be commented out on js warnings
  "-Xlint:_,-missing-interpolator,-adapted-args"
)

def crossLibs(configuration: Configuration) =
  libraryDependencies ++= crossDeps.value.map(_ % configuration)

lazy val `stamp-clock` = project.in(file("."))
  .aggregate(sharedJS, sharedJVM, frontend, backend)
  .dependsOn(backend)
  .settings(
    publishArtifact := false,
    mainClass in Compile := Some("org.codeape.sc.backend.Launcher")
  )

lazy val shared = crossProject.crossType(CrossType.Pure).in(file("shared"))
  .settings(
    crossLibs(Provided)
  )

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js

lazy val backend = project.in(file("backend"))
  .dependsOn(sharedJVM)
  .settings(
    libraryDependencies ++= backendDeps.value,
    crossLibs(Compile),

    compile := (compile in Compile).value,
    (compile in Compile) := (compile in Compile dependsOn copyStatics).value,
    copyStatics := IO.copyDirectory((crossTarget in frontend).value / StaticFilesDir, (target in Compile).value / StaticFilesDir),
    copyStatics := (copyStatics dependsOn (compileStatics in frontend)).value,

    mappings in (Compile, packageBin) ++= {
      copyStatics.value
      ((target in Compile).value / StaticFilesDir).***.get map { file =>
        file -> file.getAbsolutePath.stripPrefix((target in Compile).value.getAbsolutePath)
      }
    },

    watchSources ++= (sourceDirectory in frontend).value.***.get
  )

lazy val frontend = project.in(file("frontend")).enablePlugins(ScalaJSPlugin)
  .dependsOn(sharedJS)
  .settings(
    libraryDependencies ++= frontendDeps.value,
    crossLibs(Compile),
    jsDependencies ++= frontendJSDeps.value,
    scalaJSUseMainModuleInitializer in Compile := true,
    mainClass in Compile := Some("org.codeape.sc.frontend.Init"),

    compile := (compile in Compile).value,
    compileStatics := {
      IO.copyDirectory(sourceDirectory.value / "main/assets/fonts", crossTarget.value / StaticFilesDir / WebContent / "assets/fonts")
      IO.copyDirectory(sourceDirectory.value / "main/assets/images", crossTarget.value / StaticFilesDir / WebContent / "assets/images")
      compileStaticsForRelease.value
      (crossTarget.value / StaticFilesDir).***.get
    },
    compileStatics := (compileStatics dependsOn (compile in Compile)).value,

    artifactPath in(Compile, fastOptJS) :=
      (crossTarget in(Compile, fastOptJS)).value / StaticFilesDir / WebContent / "scripts" / "frontend-impl-fast.js",
    artifactPath in(Compile, fullOptJS) :=
      (crossTarget in(Compile, fullOptJS)).value / StaticFilesDir / WebContent / "scripts" / "frontend-impl.js",
    artifactPath in(Compile, packageJSDependencies) :=
      (crossTarget in(Compile, packageJSDependencies)).value / StaticFilesDir / WebContent / "scripts" / "frontend-deps-fast.js",
    artifactPath in(Compile, packageMinifiedJSDependencies) :=
      (crossTarget in(Compile, packageMinifiedJSDependencies)).value / StaticFilesDir / WebContent / "scripts" / "frontend-deps.js"
  )
