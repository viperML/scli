logLevel := Level.Info

lazy val stage = taskKey[File]("stage")

val http4sVersion = "0.23.26"

import scala.scalanative.build._

lazy val root = project
  .in(file("."))
  .enablePlugins(ScalaNativePlugin)
  .settings(
    scalaVersion := "3.3.1",
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "cats-core" % "2.10.0",
      "com.armanbilge" %%% "epollcat" % "0.1.6",
      "org.typelevel" %%% "cats-effect" % "3.5.3",
      "org.typelevel" %%% "cats-effect-kernel" % "3.5.3",
      "org.typelevel" %%% "cats-effect-std" % "3.5.3",
      "com.outr" %%% "scribe" % "3.13.2",
      "com.outr" %%% "scribe-cats" % "3.13.2",
      "org.http4s" %%% "http4s-ember-client" % http4sVersion,
      // "org.http4s" %%% "http4s-ember-server" % http4sVersion,
      "org.http4s" %%% "http4s-dsl" % http4sVersion
    ),
    nativeConfig ~= { c =>
      c.withLTO(LTO.none) // thin
        // .withMode(Mode.debug) // releaseFast
        .withMode(Mode.debug)
        .withGC(GC.immix) // commix
    },
    stage := {
      val exeFile = (Compile / nativeLink).value
      val targetFile = target.value / "scli"
      sbt.IO.copyFile(exeFile, targetFile)
      targetFile
    }
  )
