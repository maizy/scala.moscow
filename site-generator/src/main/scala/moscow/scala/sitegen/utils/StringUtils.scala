package moscow.scala.sitegen.utils

import scala.util.control.Exception

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */
object StringUtils {
  def toIntOpt(s: String): Option[Int] =
    Exception.catching(classOf[NumberFormatException]) opt { s.toInt }
}
