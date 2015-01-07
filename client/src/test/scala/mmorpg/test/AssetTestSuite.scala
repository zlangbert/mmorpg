package mmorpg.test

import mmorpg.assets.SpriteLoader
import utest._

import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object AssetTestSuite extends TestSuite {

  val tests = TestSuite {
    "async" - {
      Future {
        assert(true)
      }
    }
    // i cant figure out how to do async tests. maybe you cant
    /*"load a sprite" - {
      SpriteLoader("clotharmor").map { sprite =>
        assert(sprite != null)
      }
    }*/
  }
}