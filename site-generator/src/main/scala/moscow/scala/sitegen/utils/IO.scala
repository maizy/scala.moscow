package moscow.scala.sitegen.utils

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.File

object IO {

  def getResourceAsFile[T](clazz: Class[T], path: String): Option[File] =
    Option(clazz.getResource(path)) map { u => new File(u.toURI) }

}
