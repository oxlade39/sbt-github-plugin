sbtPlugin := true

name := "sbt-github-plugin"

version := "2.0-SNAPSHOT"

organization := "com.github.oxlade39"

scalaVersion := "2.10.4"

resolvers += "Eclipse Project JGit repo" at "http://download.eclipse.org/jgit/maven"

resolvers += "Scala Snapshots repo" at "http://scala-tools.org/repo-snapshots"

libraryDependencies ++= Seq(
  "org.eclipse.jgit" % "org.eclipse.jgit" % "0.9.3",
  "commons-httpclient" % "commons-httpclient" % "3.1",
  "junit" % "junit" % "4.7" % "test",
  "org.scala-tools.testing" %% "specs" % "1.6.9" % "test",
   "org.mockito" % "mockito-core" % "1.10.8" % "test"
)
