package moscow.scala

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.nio.file.Paths
import com.typesafe.scalalogging.Logger
import org.json4s.JsonDSL._
import moscow.scala.sitegen.{ Storage, PageBuilder }
import org.slf4j.LoggerFactory


object SiteGeneratorApp extends App {

  val logger = Logger(LoggerFactory.getLogger("scala.moscow.SiteGenerator"))

  //TODO: opt parsing
  val storagePath = Paths.get("temp").toAbsolutePath.normalize()


  logger.info("generate scala.moscow site")

  logger.info("load common context")
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

  val storage = Storage(storagePath)

  logger.info("create pages")
  PageBuilder("index", storage)
    .updateContext(data)
    .saveAs(Paths.get("index.html"))

  PageBuilder("about", storage)
    .updateContext(data)
    .saveAs(Paths.get("about.html"))

}
