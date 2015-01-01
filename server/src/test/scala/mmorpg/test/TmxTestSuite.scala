package mmorpg.test

import java.io.File

import mmorpg.tmx.TmxLoader
import utest._

object TmxTestSuite extends TestSuite {
  val tests = TestSuite {
    "load a tmx map" - {

      val file = getClass.getResource("/maps/test.json").getFile
      val path = new File(file).toPath
      val map = TmxLoader.load(path)

      assert(map.version == 1)
      assert(map.orientation == "orthogonal")
      assert(map.width == 20)
      assert(map.height == 20)

      "load tilesets" - {
        assert(map.tilesets.size > 0)
        assert(map.tilesets.head.name == "tileset")
      }
    }
  }
}