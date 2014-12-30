package mmorpg.test

import java.io.File
import java.nio.file.{FileSystems, Path, Files}

import mmorpg.tmx.TmxLoader
import utest._

object TmxTestSuite extends TestSuite {
  val tests = TestSuite {
    "load a tmx map" - {

      val file = getClass.getResource("/maps/test.json").getFile
      val path = new File(file).toPath
      TmxLoader.load(path)
    }
  }
}
