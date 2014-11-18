package com.github.oxlade39.githubuploader

import java.io.File

import sbt._
import Keys._

object GitHubPlugin extends AutoPlugin {
  override def trigger = allRequirements

  object autoImport {
    lazy val gitHubPublish = taskKey[Unit]("publish artifacts to github")
  }
  import autoImport._
  lazy val baseObfuscateSettings: Seq[Def.Setting[_]] = Seq(
    gitHubPublish := GitHubPublish(
      (artifactPath in packageBin).value,
      (artifacts in gitHubPublish).value
    ),
    sources in gitHubPublish := sources.value
  )
}

// core feature implemented here
object GitHubPublish {
  def apply(outputPath: File, artifacts: Seq[Artifact]): Unit = {
    artifacts.foreach { a =>
      val artifactFile = new File(outputPath, a.name + "-" + version.toString + "." + a.extension)
        uploader.upload(Upload(artifactFile, artifactFile.getName, "Uploaded by the sbt-github-plugin"))
    }
  }
}