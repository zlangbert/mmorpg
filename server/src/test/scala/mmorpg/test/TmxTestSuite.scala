package mmorpg.test

import mmorpg.tmx.TmxLoader
import utest._

import scala.concurrent.Await
import scala.concurrent.duration._

object TmxTestSuite extends TestSuite {
  val tests = TestSuite {
    "load a tmx map" - {

      val map = Await.result(TmxLoader.load("test"), 1.second)

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