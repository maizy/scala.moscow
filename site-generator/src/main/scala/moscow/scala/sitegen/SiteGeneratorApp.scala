package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import scala.io.Source
import com.gilt.handlebars.scala.binding.dynamic._
import com.gilt.handlebars.scala.Handlebars

import moscow.scala.sitegen.utils.IO.getResourceAsFile

object SiteGeneratorApp extends App {

  println("Try handlebars template")

  object TestData {
    val name = "Alan"
    val hometown = "Somewhere, TX"
    val copyright = Map(
      "year" -> 2015
    )
    val kids = Seq(Map(
      "name" -> "Jimmy",
      "age" -> "12"
    ), Map(
      "name" -> "Sally",
      "age" -> "4"
    ))
  }

  println("SAMPLE 1")
  getResourceAsFile(getClass, "/templates/main.handlebars") foreach { template =>
    val templateProcessor = Handlebars(template)
    println(templateProcessor(TestData))
  }

  println("SAMPLE 2")
  getResourceAsFile(getClass, "/templates/main2.handlebars") foreach { template =>
    val templateProcessor = Handlebars(template)
    println(templateProcessor(TestData))
  }

}
