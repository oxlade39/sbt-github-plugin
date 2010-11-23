package com.github.oxlade39.githubuploader

sealed case class GitHubConfig(login: String, token: String, repository: GitHubRepository) {
	def repositoryName = repository.repositoryName
}

abstract class GitHubConfigFactory extends RequiresJGitRepository{
  import JGitRepositoryUtils.repositoryAccess

  def apply(): GitHubConfig = new GitHubConfig(repository.login, repository.token, gitHubRepository)

  def gitHubRepository = GitHubRepositoryExtractor(repository.remoteURL).getOrElse {
    throw new RuntimeException("Couldn't determine url")
  }
}

object DefaultGitHubConfigFactory extends GitHubConfigFactory with JGitFileRepositoryProvider

