package com.github.oxlade39.githubuploader

import sbt._

trait ArtifactFinder { self: Project =>
  def artifacts: Set[Artifact]

  def artifactFiles: Set[java.io.File] = artifacts.map { a =>
    (outputPath / (a.name + "-" + version.toString + "." + a.extension)).asFile
  }
}

trait GitHubUploadedArtifacts extends Project with ArtifactFinder {
 def artifacts: Set[sbt.Artifact]
 def projectID : sbt.ModuleID

 def configure: Unit = {
	JGitRepositoryUtils.customConfig = customConfiguration
 }

 def customConfiguration: Option[SbtGitHubPluginConfig] = None

 lazy val gitHubUpload = task {
	for(artifactFile <- artifactFiles) {
 		uploader.upload(Upload(artifactFile, artifactFile.getName, "Uploaded by the sbt-github-plugin"))
	}
	None
 } describedAs ( "Upload all SBT artifacts to your GitHub repository downloads" )
}

object uploader extends HttpGitHubUpload with DefaultGitHubConfigProvider {
	val http = HttpClientHttp
}