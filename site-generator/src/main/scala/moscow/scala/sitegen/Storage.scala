package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.{ IOException, InputStream }
import java.nio.file.{ Files, Path }
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
    if (Files.exists(absPath)) {
      if (!Files.isDirectory(absPath)) {
        throw new StorageException(s"Could't create dir $path in storage, path is exist and not a dir")
      }
    } else {
      wrapIOException(s"Unable to create directory $path") {
        Files.createDirectory(absPath)
      }
    }
  }


  @throws(classOf[StorageException])
  private def createParentDirsIfNotExist(path: Path): Unit = {
    require(!path.isAbsolute)
    Option(path.getParent).foreach { parent =>
      wrapIOException(s"Unable to create directories for $path") {
        Files.createDirectories(basePath.resolve(parent))
      }
    }
  }


  @throws(classOf[StorageException])
  def createFile(content: String, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    val absPath: Path = resolveAndCheckPath(relPath)
    createParentDirsIfNotExist(relPath)
    writeToPath(absPath) { w =>
      w.write(content)
      logger.info(s"File created $relPath (${content.length} bytes)")
    } recover {
      case e: IOException =>  throw new StorageException(s"Unable to write file $relPath to storage", e)
    }
  }

  @throws(classOf[StorageException])
  def createFile(stream: InputStream, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    val fullPath = resolveAndCheckPath(relPath)
    createParentDirsIfNotExist(relPath)
    wrapIOException(s"Unable to create file from input stream") {
      Files.copy(stream, fullPath)
      logger.info(s"Copied input stream to $relPath")
    }
  }

  @throws(classOf[StorageException])
  def copyFileToStorage(originalAbsPath: Path, relPath: Path): Unit = {
    require(!relPath.isAbsolute)
    require(originalAbsPath.isAbsolute)
    val absFilePath: Path = resolveAndCheckPath(relPath)
    createParentDirsIfNotExist(relPath)
    wrapIOException(s"Unable to write file $relPath to storage") {
      Files.copy(originalAbsPath, absFilePath)
      logger.info(s"Copied ${originalAbsPath.getFileName} to $relPath")
    }
  }

  @throws(classOf[StorageException])
  private def resolveAndCheckPath(relPath: Path): Path = {
    val absFilePath = basePath.resolve(relPath)
    if (Files.exists(absFilePath)) {
      throw new StorageException(s"Other file exists in the same path in storage: $relPath")
    }
    absFilePath
  }

  @throws(classOf[StorageException])
  private def wrapIOException(msg: String)(f: => Unit): Unit = {
    Try(f) recover {
      case e: IOException =>
        logger.error(msg, e)
        throw new StorageException(msg, e)
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
