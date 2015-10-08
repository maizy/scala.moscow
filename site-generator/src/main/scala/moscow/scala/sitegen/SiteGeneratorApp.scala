package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import com.github.jknack.handlebars.context.MethodValueResolver
import com.github.jknack.handlebars.{ Handlebars, Context }
import com.github.jknack.handlebars.io.{ ClassPathTemplateLoader, TemplateLoader }
import org.json4s.JsonDSL._
import moscow.scala.sitegen.utils.template.JValueResolver

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
  val mainPage = templateProcessor.compile("main").apply(context)
  
  val aboutPage = templateProcessor.compile("about").apply(context)

  println(mainPage)
  println(aboutPage)
}
