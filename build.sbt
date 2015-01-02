lazy val mmorpg = (project in file("."))
                  .aggregate(client, server)
                  .dependsOn(server)
                  .settings(sharedSettings: _*)

mainClass in Compile := Some("mmorpg.Server")

Revolver.settings

lazy val sharedSettings = Seq(
  scalaVersion := "2.11.4",
  unmanagedSourceDirectories in Compile +=
    baseDirectory.value  / "shared" / "main" / "scala",
  unmanagedResourceDirectories in Compile +=
    baseDirectory.value  / "shared" / "main" / "resources",
  resolvers ++= Seq(
    Resolver.sonatypeRepo("snapshots"),
    "spray" at "http://repo.spray.io",
    "spray nightly" at "http://nightlies.spray.io/"),
  libraryDependencies ++= Seq(
    "com.scalatags" %%% "scalatags" % "0.4.3-M3",
    "com.lihaoyi" %%% "upickle" % "0.2.6-M3",
    "com.lihaoyi" %% "utest" % "0.2.5-M3" % "test"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
)

lazy val client = project.in(file("client"))
  .enablePlugins(ScalaJSPlugin)
  .settings(sharedSettings:_*)
  .settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.7.0"
    )
  )

lazy val server = project.in(file("server"))
  .settings(sharedSettings:_*)
  .settings(
    libraryDependencies ++= Seq(
      "io.spray" %% "spray-can" % "1.3.2",
      "io.spray" %% "spray-routing" % "1.3.2",
      "com.wandoulabs.akka" %% "spray-websocket" % "0.1.4-SNAPSHOT",
      "com.typesafe.akka" %% "akka-actor" % "2.3.8"
    ),
    (resources in Compile) += {
      (fastOptJS in (client, Compile)).value
      (artifactPath in (client, Compile, fastOptJS)).value
    }
  )