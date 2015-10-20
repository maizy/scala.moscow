package moscow.scala.sitegen

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */

import java.io.InputStream
import java.nio.file.{ Paths, Path, Files }
import java.util.jar.JarFile
import scala.annotation.tailrec
import scala.collection.JavaConversions.enumerationAsScalaIterator


class AssetsCollectorException(message: String) extends Exception(message)


class AssetsCollector(val storage: Storage) {

  @throws[AssetsCollectorException]
  def copyRecursiveFromResources(baseResourceLocation: String, relativeDistanation: Path): Unit = {
    require(!relativeDistanation.isAbsolute)
    var anyCopied = false
    walkResourcesRecursive(baseResourceLocation) { case (relPath, builder) =>
      val inputStream = builder()
      try {
        storage.createFile(inputStream, relPath)
        anyCopied = true
      } finally {
        inputStream.close()
      }
    }
    if (!anyCopied) {
      throw new AssetsCollectorException(s"None resources found at $baseResourceLocation")
    }
  }

  private def getJar: Option[JarFile] = {
    val location = getClass.getProtectionDomain.getCodeSource.getLocation.getPath
    val path = Paths.get(location)
    if (Files.isRegularFile(path)) {
      Some(new JarFile(path.toFile))
    } else {
      None
    }
  }

  private def walkResourcesRecursive(baseResourceLocation: String)(f: (Path, () => InputStream) => Unit): Unit = {
    val strippedLocation = baseResourceLocation.stripPrefix("/").stripSuffix("/")
    getJar match {
      case Some(j) =>
        enumerationAsScalaIterator(j.entries)
          .filter(je => je.getName.startsWith(strippedLocation + "/") && !je.isDirectory)
          .foreach { je =>
            val builder = () => j.getInputStream(je)
            f(Paths.get(je.getName), builder)
          }

      case None =>
        val baseResourcePath = Paths.get(getClass.getResource("/").toURI).toAbsolutePath
        Option(getClass.getResource("/" + strippedLocation))
        .map(f => Paths.get(f.toURI))
        .map(walkFileSystemRecursive)
        .foreach(
          _.foreach { path =>
            val relPath = baseResourcePath relativize path
            val builder = () => Files.newInputStream(path)
            f(relPath, builder)
          }
        )
    }
  }

  // TODO: refactor to lazy scala stream
  private def walkFileSystemRecursive(path: Path): Iterator[Path] = {

    @tailrec
    def recursive(paths: Seq[Path], acc: Iterator[Path] = Iterator.empty): Iterator[Path] = {
      paths match {
        case currentPath :: other =>
          if (Files.isDirectory(currentPath)) {
            val children = currentPath.toFile.listFiles
            val childrenFiles = children.iterator.filterNot(_.isDirectory).map(_.toPath)
            val childrenDirs = children.iterator.filter(_.isDirectory).map(_.toPath)
            recursive(other ++ childrenDirs, acc ++ childrenFiles)
          } else {
            recursive(other, acc ++ Iterator.single(currentPath))
          }

        case Nil =>
          acc
      }
    }
    recursive(Seq(path))
  }

}


object AssetsCollector {
  def apply(storage: Storage): AssetsCollector = new AssetsCollector(storage)
}
