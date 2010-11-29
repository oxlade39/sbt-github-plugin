import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info)
{
  val sbtGitHupPlugin = "com.github.oxlade39" % "sbt-github-plugin" % "2.0.1"
}