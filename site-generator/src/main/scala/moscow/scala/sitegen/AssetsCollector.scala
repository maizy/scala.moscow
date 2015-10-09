package moscow.scala.sitegen

import java.nio.file.Path

/**
 * Copyright (c) Nikita Kovaliov, maizy.ru, 2015
 * See LICENSE.txt for details.
 */
class AssetsCollector(val storage: Storage) {
  def copyRecursiveFromResources(baseResourcePath: Path, relativeDistanation: Path): Unit = {
    require(!baseResourcePath.isAbsolute)
    require(!relativeDistanation.isAbsolute)
    throw new NotImplementedError
  }
}

object AssetsCollector {
  def apply(storage: Storage): AssetsCollector = new AssetsCollector(storage)
}
