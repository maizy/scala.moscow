package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.InputStream
import java.nio.file.{Files, Path}
import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import moscow.scala.sitegen.utils.IOUtils.writeToPath

class StorageException(message: String, cause: Throwable = null) extends Exception(message, cause)  // scalastyle:ignore


class Storage(val basePath: Path) extends LazyLogging {

  require(basePath.isAbsolute)

  @throws(classOf[StorageException])
  def createDirIfNotExist(path: Path): Unit = {
    val absPath = path.isAbsolute match {
      case true => path
      case false => basePath.resolve(path)
    }
    val pathFile = absPath.toFile
    if (pathFile.exists) {
      if (!pathFile.isDirectory) {
        throw new StorageException(s"Could't create dir $path in storage, path is exist and not a dir")
      }
    } else {
      wrapException(s"Unable to create directory $path") {
        Files.createDirectory(absPath)
      }
    }
  }

  @throws(classOf[StorageException])
  def createFile(content: String, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    Option(relPath.getParent) foreach createDirIfNotExist
    val absPath: Path = resolveAndCheckPath(relPath)
    writeToPath(absPath) { w =>
      logger.info(s"Write ${content.length} bytes to $relPath")
      w.write(content)
    } recover {
      case e: Throwable =>  throw new StorageException(s"Unable to write file $relPath to storage", e)
    }
  }

  // TODO
  def createFile(stream: InputStream, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    throw new NotImplementedError
  }

  @throws(classOf[StorageException])
  def copyFileToStorage(originalAbsPath: Path, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    require(originalAbsPath.isAbsolute)
    val absFilePath: Path = resolveAndCheckPath(relPath)
    wrapException(s"Unable to write file $relPath to storage") {
      logger.info(s"Copy ${originalAbsPath.getFileName} to $relPath")
      Files.copy(originalAbsPath, absFilePath)
    }
  }

  @throws(classOf[StorageException])
  private def resolveAndCheckPath(relPath: Path): Path = {
    val absFilePath = basePath.resolve(relPath)
    if (absFilePath.toFile.exists()) {
      throw new StorageException(s"Other file exists in the same path in storage: $relPath")
    }
    absFilePath
  }

  @throws(classOf[StorageException])
  private def wrapException(msg: String)(f: => Unit): Unit = {
    Try(f) recover {
      case e: Throwable => throw new StorageException(msg, e)
    }
  }
}


object Storage {
  def apply(basePath: Path): Storage = {
    val storage = new Storage(basePath)
    storage.createDirIfNotExist(basePath)
    storage
  }
}
