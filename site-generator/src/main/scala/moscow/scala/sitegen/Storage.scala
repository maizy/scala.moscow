package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.InputStream
import java.nio.file.Path


class Storage(val basePath: Path) {

  require(basePath.isAbsolute)

  private def createDirIfNotExist(relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    throw new NotImplementedError
  }

  def createFile(content: String, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    throw new NotImplementedError
  }

  def createFile(stream: InputStream, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    throw new NotImplementedError
  }

  def copyFileToStorage(originalAbsPath: Path, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    require(originalAbsPath.isAbsolute)
    throw new NotImplementedError
  }
}


object Storage {
  def apply(basePath: Path): Storage = new Storage(basePath)
}
