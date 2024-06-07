import sbt.Keys.organization

/**
 * LibVoxbone is a library that implements Voxbone official API using Akka-Http and packaged into a jar.
 * This enables easy Voxbone methods and calls, for any java or scala project.
 */
val voxboneVersion = "1"

lazy val lib = (projectMatrix in file("lib"))
  .settings(
    name := "libvoxbone",
    organization := "dk.nuuday.digitalCommunications.voxbone",
    organizationName := "Nuuday A/S - Digital Communications",
    organizationHomepage := Some(url("https://sky.tdc.dk")),
    scalacOptions += "-deprecation",

    version := s"$voxboneVersion.0.10",

    libraryDependencies ++= {

      object V {
        val scalacticScalatest = "3.2.2"
        val akka = "2.6.10"
        val akkaHttp = "10.2.1"
      }

      Seq(
        "com.typesafe.akka"                           %% "akka-actor"                     % V.akka,
        "com.typesafe.akka"                           %% "akka-slf4j"                     % V.akka,
        "com.typesafe.akka"                           %% "akka-stream"                    % V.akka,
        "com.typesafe.akka"                           %% "akka-http-spray-json"           % V.akkaHttp,
        "com.typesafe.akka"                           %% "akka-http-xml"                  % V.akkaHttp,
        "com.typesafe.akka"                           %% "akka-http"                      % V.akkaHttp,
        "com.beachape"                                %% "enumeratum"                     % "1.6.1",
        "io.spray"                                    %% "spray-json"                     % "1.3.5",
        "com.typesafe.scala-logging"                  %% "scala-logging"                  % "3.9.2",
        "dk.cirque.util"                              %% "util-scala-logging"             % "1.0.4",
        "org.scalactic"                               %% "scalactic"                      % V.scalacticScalatest,
        "org.scalatest"                               %% "scalatest"                      % V.scalacticScalatest % Test,
        "net.jadler"                                  % "jadler-all"                      % "1.3.0"  % Test,
        "org.scalamock"                               %% "scalamock"                      % "4.4.0"  % Test,
        "com.typesafe.scala-logging"                  %% "scala-logging"                  % "3.9.2"
      )
    },
    externalResolvers := {
      externalResolvers.value.filter(_.name == "local") ++ Seq(
        "Cirque repo" at "https://nexus.cirque-udv.dk/repository/cirque.default/")
    },
    credentials += Credentials(Path.userHome / ".sbt" / "credentials"),
    credentials += {
      Option(System.getenv("SBT_CIRQUE_NEXUS_USER")) map { envUserName =>
        Option(System.getenv("SBT_CIRQUE_NEXUS_PASSWORD"))
          .map(Credentials("Sonatype Nexus Repository Manager", "repo.cirque.dk",
            envUserName, _))
          .getOrElse(throw new IllegalStateException(
            "There is a SBT_CIRQUE_NEXUS_USER env variable, but no SBT_CIRQUE_NEXUS_PASSWORD"))
      } getOrElse {
        val f = Path.userHome / ".sbt" / "credentials"
        if (!f.exists())
          throw new IllegalStateException(
            s"Credentials for our nexus repo, is not available, either as env variables, or from file: ${f.getAbsolutePath}")
        Credentials(f)
      }
    },

    scalacOptions += "-Xfatal-warnings",
    //scalacOptions += "-Ylog-classpath",
    //scalaSource in Test := baseDirectory.value / "test",

    publishMavenStyle := true,
    crossPaths := true,
    publishTo := {
      if (version.value.trim.toUpperCase.contains("SNAPSHOT"))
        Some(
          "snapshots" at "https://nexus.cirque-udv.dk/repsitory/internal.snapshots/")
      else
        Some("internal" at "https://nexus.cirque-udv.dk/repository/internal/")
    }
  ).crossLibrary(
  scalaVersions = Seq("2.13.2"),
  suffix = "2.13",
  settings = Seq(
    crossPaths := false //Done to get the right grama in the library version name
    )
  )
// As the lib lives in a subproject, disable publish on the root project.
publish := {}
scalaVersion := "2.13.0"
