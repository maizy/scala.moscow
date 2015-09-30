name := "scala-moscow-sitegen"

enablePlugins(GitVersioning)

git.baseVersion := "0.0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "com.gilt" %% "handlebars-scala" % "2.0.1"
)
