package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import com.github.jknack.handlebars.context.MethodValueResolver
import com.github.jknack.handlebars.{ Handlebars, Context }
import com.github.jknack.handlebars.io.{ ClassPathTemplateLoader, TemplateLoader }
import moscow.scala.sitegen.utils.template.JValueResolver

import org.json4s.native.JsonMethods
import org.json4s.JsonDSL._

object SiteGeneratorApp extends App {

  println("Try handlebars template")

  val testData =
    ("name" -> "Nikita") ~
    ("hometown" -> "Moscow") ~
    ("copyright" ->
      ("year" -> "2015") ~
      ("some" -> "2014")
    ) ~
    ("children" ->
      Seq(
        ("name" -> "Matthew") ~ ("age" -> 5),
        ("name" -> "Veronika") ~ ("age" -> 1)
      )
    )

  println(s"TestData = $testData")
  println("CONTEXT: " + JsonMethods.pretty(JsonMethods.render(testData)))

  val loader: TemplateLoader = new ClassPathTemplateLoader()
  loader.setPrefix("/templates")
  loader.setSuffix(".handlebars")
  val templateProcessor = new Handlebars(loader)
  val context = Context.newBuilder(testData)
    .resolver(
      JValueResolver.INSTANCE,
      MethodValueResolver.INSTANCE
    )
    .build
  val template = templateProcessor.compile("with-partial")
  println(template.apply(context))
}
