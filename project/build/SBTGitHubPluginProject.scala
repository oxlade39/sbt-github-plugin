import sbt._
import com.github.oxlade39.githubuploader.GitHubUploadedArtifacts

class SBTGitHubPluginProject(info: ProjectInfo) extends PluginProject(info) with Exec with GitHubUploadedArtifacts {


//  override def buildScalaVersion = "2.8.1"
  // this restrict the executed classes names to end with either "Spec" or "Unit"
  override def includeTest(s: String) = { s.endsWith("Spec") || s.endsWith("Unit") }

  val jgitRepository = "Eclipse Project JGit repo" at "http://download.eclipse.org/jgit/maven"
  val scalaSnapshots = "Scala Snapshots repo" at "http://scala-tools.org/repo-snapshots"

  val jgitCore = "org.eclipse.jgit" % "org.eclipse.jgit" % "0.9.3" withSources
  val commonsHttp = "commons-httpclient" % "commons-httpclient" % "3.1"

  val junit = "junit" % "junit" % "4.7" % "test" withSources
//  val specs = "org.scala-tools.testing" % "specs_2.8.1" % "1.6.7-SNAPSHOT" % "test" withSources
  val specs = "org.scala-tools.testing" % "specs" % "1.6.1" % "test" withSources
  val mockito = "org.mockito" % "mockito-all" % "1.8.0" % "test" withSources
  val hamcrest = "org.hamcrest" % "hamcrest-all" % "1.1" % "test" withSources

}