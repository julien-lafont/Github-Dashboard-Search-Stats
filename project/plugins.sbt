// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository 
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//autoCompilerPlugins := false

// Use the Play sbt plugin for Play projects
addSbtPlugin("play" % "sbt-plugin" % "2.0")

//addCompilerPlugin("com.github.aloiscochard.sindi" %% "sindi-compiler" % "0.5")