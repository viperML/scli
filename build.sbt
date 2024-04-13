logLevel := Level.Info

lazy val stage = taskKey[File]("stage")

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
    ),

    nativeConfig ~= { c =>
      c.withLTO(LTO.thin) // thin
        // .withMode(Mode.debug) // releaseFast
        .withMode(Mode.releaseFast)
        .withGC(GC.immix) // commix
    },

    stage := {
      val exeFile = (Compile / nativeLink).value
      val targetFile = target.value / "scli"
      sbt.IO.copyFile(exeFile, targetFile)
      targetFile
    },
  )
