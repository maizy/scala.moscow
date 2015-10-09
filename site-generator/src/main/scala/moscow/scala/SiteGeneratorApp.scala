package moscow.scala

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.nio.file.Paths
import org.json4s.JsonDSL._
import moscow.scala.sitegen.{ Storage, PageBuilder }


object SiteGeneratorApp extends App {

  println("Try handlebars template")

  val data =
    ("copyright" ->
      ("year" -> "2015")
    ) ~
    ("title" -> "scala.moscow") ~  // default title
    ("pages" ->
      ("main" ->
        ("id" -> "main")
      ) ~
      ("about" ->
        ("id" -> "about") ~
        ("title" -> "scala.moscow :: о проекте")
      )
    )

  val storage = Storage(Paths.get("temp").toAbsolutePath.normalize())

  PageBuilder("index", storage)
    .updateContext(data)
    .saveAs(Paths.get("index.html"))

  PageBuilder("about", storage)
    .updateContext(data)
    .saveAs(Paths.get("about.html"))

}
