import sbt._
class Plugins(info: ProjectInfo) extends PluginDefinition(info)
{
  val a = "com.github.oxlade39" % "sbt-github-plugin" % "1.0"
}