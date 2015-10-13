enablePlugins(GitVersioning)
enablePlugins(JavaAppPackaging)

name := "scala-moscow-sitegen"
organization := "scala.moscow"
git.baseVersion := "0.0.1"

scalaVersion := "2.11.7"
libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "com.github.jknack" % "handlebars" % "2.2.2",
  "org.json4s" %% "json4s-native" % "3.3.0",
  "com.github.scopt" %% "scopt" % "3.3.0",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalacOptions ++= Seq(
  "-target:jvm-1.7",
  "-encoding", "UTF-8",
  "-deprecation",
  "-unchecked",
  "-explaintypes",
  "-Xfatal-warnings",
  "-Xlint"
)

// scalastyle
lazy val testScalastyle = taskKey[Unit]("testScalastyle")
lazy val testScalastyleInCompile = taskKey[Unit]("testScalastyleInCompile")
testScalastyle := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
testScalastyleInCompile := org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Compile).toTask("").value
(test in Test) <<= (test in Test) dependsOn (testScalastyle, testScalastyleInCompile)
scalastyleFailOnError := true
