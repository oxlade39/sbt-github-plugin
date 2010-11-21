import sbt._

class GitHubUploader(info: ProjectInfo) extends PluginProject(info) with Exec {


  override def buildScalaVersion = "2.8.1"

  val jgitRepository = "Eclipse Project JGit repo" at "http://download.eclipse.org/jgit/maven"

  val jgitCore = "org.eclipse.jgit" % "org.eclipse.jgit" % "0.9.3" withSources

  val junit = "junit" % "junit" % "4.7" % "test" withSources
  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.6" % "test" withSources
  val mockito = "org.mockito" % "mockito-all" % "1.8.4" % "test" withSources
}