name := "scala-moscow-sitegen"

enablePlugins(GitVersioning)

git.baseVersion := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.github.jknack" % "handlebars" % "2.2.2",
  "org.json4s" %% "json4s-native" % "3.3.0",

  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)
