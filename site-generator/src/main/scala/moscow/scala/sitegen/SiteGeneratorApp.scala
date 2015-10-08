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

  val data =
    ("copyright" ->
      ("year" -> "2015")
    ) ~
    ("pages" ->
      ("main" ->
        ("id" -> "main") ~
        ("title" -> "scala.moscow")
      ) ~
      ("about" ->
        ("id" -> "about") ~
        ("title" -> "scala.moscow :: о проекте")
      )
    )

  val loader: TemplateLoader = new ClassPathTemplateLoader()
  loader.setPrefix("/templates")
  loader.setSuffix(".handlebars")
  val templateProcessor = new Handlebars(loader)
  val context = Context.newBuilder(data)
    .resolver(
      JValueResolver.INSTANCE,
      MethodValueResolver.INSTANCE
    )
    .build
  val template = templateProcessor.compile("main")
  println(template.apply(context))
}
