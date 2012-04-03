import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "zenexigit"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.github.aloiscochard.sindi" %% "sindi-core" % "0.5",
      "joda-time" % "joda-time" % "2.1"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"     
    )

}
