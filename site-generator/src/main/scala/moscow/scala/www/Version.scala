package moscow.scala.www

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */
object Version {
  val VERSION_TUPLE: (Int, Int, Option[Int]) = (0, 1, None)
  val VERSION_STRING: String = {
    val major = s"${VERSION_TUPLE._1}.${VERSION_TUPLE._2}"
    if (VERSION_TUPLE._3.isDefined) {
      s"$major.${VERSION_TUPLE._3.get}"
    } else {
      major
    }
  }

  override def toString: String = VERSION_STRING
}
