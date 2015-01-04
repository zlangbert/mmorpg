package mmorpg

import scalatags.Text.all._

object Index {

  val boot = "mmorpg.Client().main(document.getElementById('contents'))"

  val skeleton =
    html(style:="height:100%",
      head(
        script(src:="/public/client-fastopt.js"),
        link(
          rel:="stylesheet",
          href:="https://cdnjs.cloudflare.com/ajax/libs/pure/0.5.0/pure-min.css"
        )
      ),
      body(style:="height:100%",
        onload:=boot,
        div(id:="header"),
        div(id:="contents", style:="height:100%",
          canvas(id:="canvas")
        ),
        div(id:="footer"),
        script(src:="//cdnjs.cloudflare.com/ajax/libs/stats.js/r11/Stats.min.js")
      )
    )
}