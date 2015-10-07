package moscow.scala.sitegen.tests.utils.template

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import com.github.jknack.handlebars.io.{ ClassPathTemplateLoader, TemplateLoader }
import com.github.jknack.handlebars.{ Context, Handlebars }
import org.json4s.{ JValue, JNull, JNothing }
import org.json4s.JsonDSL._
import moscow.scala.sitegen.utils.template.JValueResolver
import moscow.scala.sitegen.tests.BaseSpec

// based on
// https://github.com/mfirry/handlebars-json4s/blob/master/src/test/scala/com/github/mfirry/handlebars/json4s/JValueResolverTest.scala
class JValueResolverSpec extends BaseSpec {

  val primitiveTypes =
    ("string" -> "abc") ~
    ("int" -> 678) ~
    ("long" -> 6789L) ~
    ("float" -> 7.13f) ~
    ("double" -> 3.14d) ~
    ("bool" -> true) ~
    ("nothing" -> JNothing) ~
    ("null" -> JNull)

  "JValueResolver" should "resolve primitive json types" in {
    processInline(
      "{{string}} {{int}} {{long}} {{float}} {{double}} {{bool}} :{{nothing}}: :{{null}}:",
      primitiveTypes
    ) should be("abc 678 6789 7.130000114440918 3.14 true :: ::")
  }

  it should "resolve primitive type included in object" in {
    val json = ("key" -> primitiveTypes)
    processInline(
      "{{key.string}} {{key.int}} {{key.long}} {{key.float}} {{key.double}}" +
        " {{key.bool}} :{{key.nothing}}: :{{key.null}}:",
      json
    ) should be("abc 678 6789 7.130000114440918 3.14 true :: ::")
  }

  it should "resolve json list by index" in {
    val json =
        ("myList" -> Seq("a", "b", "c")) ~
        ("first" -> "1st-level")
    val templateProcessor = new Handlebars()
    val context = Context.newBuilder(json).resolver(JValueResolver.INSTANCE).build

    processInline(
      "a={{myList.0}} b={{myList.1}} none=|{{myList.4}}| wrong=|{{myList.some}}|",
      json
    ) should be("a=a b=b none=|| wrong=||")
  }

  val severalLevelJson =
    ("second" ->
      ("level" -> "second") ~
      ("levelInt" -> 2) ~
      ("deep" -> ("inside" -> "value")) ~
      ("deepList" -> Seq(1, 2, 3, 4))
    ) ~
    ("first" -> "1st-level")

  it should "resolve second level objects" in {
    processInline(
      "{{#with second}}level: {{level}} - {{levelInt}}{{/with}} + {{first}}",
      severalLevelJson
    ) should be("level: second - 2 + 1st-level")
  }

  it should "work in partials with context passing" in {
    processTemplate("with-partial", severalLevelJson) should be(
      "first: 1st-level\nlevel: second - 2\n\n")
  }

  it should "ignore wrong keys" in {
    processInline(
      "{{third}}{{second.1}}{{second.level.wrong}}{{second.levelInt.wrong}}",
      severalLevelJson
    ) should be("")
  }
  
  it should "ignore output of non primitive values" in {
    processInline(
      "{{second}}{{second.deep}}{{second.deepList}}",
      severalLevelJson
    ) should be("")
  }
  // other bad cases (context as is ...)

  private def processInline(template: String, data: JValue): String = {
    val templateProcessor = new Handlebars()
    val context = Context.newBuilder(data).resolver(JValueResolver.INSTANCE).build
    templateProcessor.compileInline(template).apply(context)
  }

  private def processTemplate(name: String, data: JValue): String = {
    val resourceLoader: TemplateLoader = new ClassPathTemplateLoader
    resourceLoader.setPrefix("/templates")
    resourceLoader.setSuffix(".handlebars")
    val templateProcessor = new Handlebars(resourceLoader)
    val context = Context.newBuilder(data).resolver(JValueResolver.INSTANCE).build
    templateProcessor.compile(name).apply(context)
  }
}
