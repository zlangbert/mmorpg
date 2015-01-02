mmorpg
======

Generic browser MMORPG using Scala and Scala.js

#### Building

Before this will build you need to symlink `client/shared` to `server/shared`. Due to the way Scala.js works shared code
can not be a seperate project and instead must be a source root for both the jvm and js projects. It would be nice to have
the shared directory in the root but then IntelliJ complains. Hopefully there will be a better solution to this soon.
