package moscow.scala.www

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.nio.file.{Path, Paths}
import com.typesafe.scalalogging.Logger
import moscow.scala.sitegen.{PageBuilder, Storage, StorageException}
import org.json4s.JsonDSL._
import org.slf4j.LoggerFactory


object ScalaMoscowSiteGeneratorApp extends App {

  val SITEGEN_ERROR = 1
  val BAD_ARGUMENTS_ERROR = 2

  case class AppOptions(basePath: Path = Paths.get("./"), cmd: Option[String] = None)

  object OptionParser {
    private val parser = new scopt.OptionParser[AppOptions]("java -jar scala-moscow-sitegen.jar") {
      head("scala.moscow site generator", Version.VERSION_STRING)
      help("help")
      version("version")

      (cmd("gen")
        action { (_, opts) => opts.copy(cmd = Some("gen")) }
        text { "generate site" }
        children(
          opt[String]('o', "output")
            text { "base output path, will be created if not exist" }
            required()
            action { (value, opts) =>
              val path = Paths.get(value).toAbsolutePath.normalize
              opts.copy(basePath = path)
          }
        )
      )
    }

    def parse(args: Seq[String]): Option[AppOptions] = {
      val opts = parser.parse(args, AppOptions())
      if (opts.isEmpty || opts.get.cmd.isEmpty) {
        parser.showUsageAsError
        None
      } else {
        opts
      }
    }
  }

  val logger = Logger(LoggerFactory.getLogger("scala.moscow.SiteGenerator"))

  val mayBeOpts = OptionParser.parse(args)
  if (mayBeOpts.isEmpty) {
    System.exit(BAD_ARGUMENTS_ERROR)
  }
  val opts = mayBeOpts.get

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

  try {
    logger.debug(s"Base output path ${ opts.basePath }")
    val storage = Storage(opts.basePath)

    logger.info("create pages")
    PageBuilder("index", storage)
      .updateContext(data)
      .saveAs(Paths.get("index.html"))

    PageBuilder("about", storage)
      .updateContext(data)
      .saveAs(Paths.get("about.html"))
  } catch {
    case e: StorageException =>
      logger.error("Unable to generate site. storage error", e)
      System.exit(SITEGEN_ERROR)
  }

}
