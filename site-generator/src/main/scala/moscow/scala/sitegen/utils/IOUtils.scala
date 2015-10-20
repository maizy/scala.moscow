package moscow.scala.sitegen.utils

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.{ IOError, PrintWriter }
import java.nio.file.Path
import scala.util.{ Failure, Success, Try }


object IOUtils {

  def writeToPath(path: Path)(f: PrintWriter => Unit): Try[Boolean] = {
    val writer = new PrintWriter(path.toFile)
    try {
      f(writer)
      Success(true)
    } catch {
      case e: IOError => Failure[Boolean](e)
    } finally {
      writer.close()
    }
  }

}
