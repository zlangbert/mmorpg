mmorpg
======

A generic browser based MMORPG using Scala and Scala.js

#### Building

Before this will build you need to symlink `client/shared` to `server/shared`. Due to the way Scala.js works shared code
can not be a seperate project and instead must be a source root for both the jvm and js projects. It would be nice to have
the shared directory in the root but then IntelliJ complains. Hopefully there will be a better solution to this soon.

#### Running

During development the easiest thing to do is launch sbt and `~reStart`. This will run the server and recompile/restart on changes.
