package com.github.oxlade39.githubuploader

import java.io.File
import org.eclipse.jgit.lib.Repository
sealed case class GitHubConfig(login: String, token: String, repositoryName: String)

abstract class GitHubConfigFactory {
  import JGitRepositoryUtils.repositoryAccess

  def repository: Repository

  def apply(): GitHubConfig = {
    new GitHubConfig(login(repository), token(repository), repoName(repository))
  }

  def login(repo: Repository): String = repo.login
  def token(repo: Repository): String = repo.token
  def repoName(repo: Repository): String =
    GitHubRepositoryExtractor(repo.remoteURL).getOrElse {
      throw new RuntimeException("Couldn't determine url")
    }.repositoryName

}

object JGitRepositoryUtils {
  class RepositoryWrapper(repository: Repository) {
   def login: String = repository.getConfig.getString("github", null, "user")
   def token: String = repository.getConfig.getString("github", null, "token")
   def remoteURL: String = repository.getConfig.getString("remote", "origin", "url")
  }

  implicit def repositoryAccess(repository: Repository): RepositoryWrapper = new RepositoryWrapper(repository)
}



sealed case class GitHubFile(localFile: File, description: String)

