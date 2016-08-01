name := "rapture-101"

organization := "org.zy"

version := "1.0.0"

scalaVersion := "2.11.8"

resolvers ++= Seq(
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
)

libraryDependencies ++= Seq(
  "com.propensive" %% "rapture-http" % "2.0.0-M6",
  "com.propensive" %% "rapture-http-json" % "2.0.0-M6",
  "com.propensive" %% "rapture-io" % "2.0.0-M6",
  "com.propensive" %% "rapture-uri" % "2.0.0-M6",
  "com.propensive" %% "rapture-net" % "2.0.0-M6",
  "com.propensive" %% "rapture-json" % "2.0.0-M6",
  "com.propensive" %% "rapture-json-jawn" % "2.0.0-M6",
  "com.propensive" %% "rapture-json-jackson" % "2.0.0-M6"
)

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-language:implicitConversions",
  "-language:experimental.macros",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint",
  "-Yinline-warnings",
  "-Ywarn-dead-code",
  "-Xfuture")

initialCommands := "import org.zy.reactors1._"
