package com.github.oxlade39.githubuploader

trait SbtGitHubPluginConfig {
  def githubSectionName: String
  def remoteSectionName: String
  def remoteBranchName: String
}

object DefaultSbtGitHubPluginConfig extends SbtGitHubPluginConfig {
  def githubSectionName = "github"
  def remoteSectionName = "remote"
  def remoteBranchName = "origin"
}
