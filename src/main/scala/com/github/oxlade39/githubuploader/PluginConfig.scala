package com.github.oxlade39.githubuploader

case class SbtGitHubPluginConfig(
	githubSectionName: String,
	remoteSectionName: String,
	remoteBranchName: String
)

object DefaultSbtGitHubPluginConfig extends SbtGitHubPluginConfig("github", "remote", "origin")
