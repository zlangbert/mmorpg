lazy val mmorpg = (project in file("."))
                  .enablePlugins(JavaServerAppPackaging)
                  .aggregate(client, server)
                  .dependsOn(server)
                  .settings(mmorpgSettings: _*)

mainClass in Compile := Some("mmorpg.Server")

Revolver.settings

val scalaJsOutputDir = Def.settingKey[File]("directory for Scala.js output")

val configurations = Seq(Universal, Debian, Windows)

/**
 * Server project
 */
lazy val server = project.in(file("server"))
  .settings(serverSettings: _*)

/**
 * Client project
 */
lazy val client = project.in(file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(clientSettings:_*)

/**
 * mmorpg settings
 */
lazy val mmorpgSettings =
  Seq(
    name := "mmorpg"
  ) ++ (
    configurations flatMap { config =>
      Seq(
        stage in config <<= (stage in config) dependsOn (fullOptJS in (client, Compile)),
        dist in config <<= (dist in config) dependsOn (fullOptJS in (client, Compile)),
        packageBin in config <<= (packageBin in config) dependsOn (fullOptJS in (client, Compile))
      )
    }
  ) ++ baseSettings

/**
 * Server settings
 */
lazy val serverSettings =
  Seq(
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-routing" % "1.3.2",
      "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4-SNAPSHOT",
      "com.typesafe.akka" %% "akka-actor" % "2.3.8"
    ),
    scalaJsOutputDir := (classDirectory in Compile).value / "public" / "js",
    compile in Compile <<= (compile in Compile) dependsOn (fastOptJS in (client, Compile))
  ) ++ {
    Seq(fastOptJS, fullOptJS) map { packageJSKey =>
      crossTarget in (client, Compile, packageJSKey) := scalaJsOutputDir.value
    }
  } ++ sharedSettings ++ utest.jsrunner.Plugin.utestJvmSettings

/**
 * Client settings
 */
lazy val clientSettings =
  Seq(
    requiresDOM := true,
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.7.0"
    )
  ) ++ sharedSettings ++ utest.jsrunner.Plugin.utestJsSettings

/**
 * Shared settings
 */
lazy val sharedSettings =
  Seq(
    unmanagedSourceDirectories in Compile +=
      baseDirectory.value  / "shared" / "main" / "scala",
    unmanagedResourceDirectories in Compile +=
      baseDirectory.value  / "shared" / "main" / "resources",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-async" % "0.9.2",
      "com.scalatags" %%% "scalatags" % "0.4.3-M3",
      "com.lihaoyi" %%% "upickle" % "0.2.6-M3"
    )
  ) ++ baseSettings

lazy val baseSettings = Seq(
  scalaVersion := "2.11.4",
  scalacOptions += "-deprecation",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots")
  )
)