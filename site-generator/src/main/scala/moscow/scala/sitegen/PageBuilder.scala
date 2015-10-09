package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.nio.file.Path
import com.github.jknack.handlebars.context.MethodValueResolver
import com.github.jknack.handlebars.{ Handlebars, Context }
import com.github.jknack.handlebars.io.{ ClassPathTemplateLoader, TemplateLoader }
import org.json4s.JsonAST.{ JNothing, JValue }
import moscow.scala.sitegen.utils.template.JValueResolver


class PageBuilder(
    val pageCode: String,
    val templateName: String,
    val storage: Storage)
{

  private var _context: JValue = JNothing

  private val loader: TemplateLoader = new ClassPathTemplateLoader
  loader.setPrefix("/templates")

  private val templateProcessor = new Handlebars(loader)

  def context: JValue = _context

  def updateContext(data: JValue): PageBuilder = {
    _context = _context merge data
    this
  }

  def loadContextFromResource(relPath: Path): PageBuilder = ???

  def saveAs(relPath: Path): Unit = {
    assert(!relPath.isAbsolute)
    val content = render()
    storage.createFile(content, relPath)
  }

  private def render(): String = {
    val templateContext = Context.newBuilder(_context)
      .resolver(
        JValueResolver.INSTANCE,
        MethodValueResolver.INSTANCE
      )
      .build

    templateProcessor.compile(templateName).apply(templateContext)
  }

}


object PageBuilder {

  def apply(pageCode: String, templateName: String, storage: Storage): PageBuilder =
    new PageBuilder(pageCode, templateName, storage)

  def apply(pageCode: String, storage: Storage): PageBuilder =
    apply(pageCode, pageCode.replace("_", "-"), storage)
}
