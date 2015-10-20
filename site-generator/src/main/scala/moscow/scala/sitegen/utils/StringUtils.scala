package moscow.scala.sitegen.utils

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import scala.util.control.Exception


object StringUtils {
  def toIntOpt(s: String): Option[Int] =
    Exception.catching(classOf[NumberFormatException]) opt { s.toInt }
}
