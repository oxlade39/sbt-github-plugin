package com.github.oxlade39.githubuploader

import java.io.File
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository

sealed case class GitHubConfig(login: String, token: String, repositoryName: String)

abstract class GitHubConfigFactory {
  import JGitRepositoryUtils.repositoryAccess

  val repository: Repository

  def apply(): GitHubConfig = {
    new GitHubConfig(repository.login, repository.token, repoName)
  }

  def gitHubRepository = GitHubRepositoryExtractor(repository.remoteURL).getOrElse {
    throw new RuntimeException("Couldn't determine url")
  }

  def repoName: String = gitHubRepository.repositoryName

}

object DefaultGitHubConfigFactory extends GitHubConfigFactory {
//  val repository = new FileRepository(System.getProperty("user.dir") + "/.git")
  val repository = new FileRepository("/Users/danoxlade/proj/scala/xmpp/scampp" + "/.git")
}

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

object JGitRepositoryUtils {
  var config: SbtGitHubPluginConfig = DefaultSbtGitHubPluginConfig

  class RepositoryWrapper(repository: Repository) {
   def login: String = repository.getConfig.getString(config.githubSectionName, null, "user")
   def token: String = repository.getConfig.getString(config.githubSectionName, null, "token")
   def remoteURL: String = repository.getConfig.getString(config.remoteSectionName, config.remoteBranchName, "url")
  }

  implicit def repositoryAccess(repository: Repository): RepositoryWrapper = new RepositoryWrapper(repository)
}



sealed case class GitHubFile(localFile: File, description: String)

