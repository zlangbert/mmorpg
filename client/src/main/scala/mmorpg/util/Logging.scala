package mmorpg.util

trait Logging {

  object Log {
    def debug(msg: String): Unit = println(s"DEBUG: $msg")
  }
}