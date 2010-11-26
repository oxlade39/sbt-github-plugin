package com.github.oxlade39.githubuploader

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository

trait RequiresJGitRepository {
	val repository: Repository
}

trait JGitFileRepositoryProvider extends RequiresJGitRepository {
 val repository = new FileRepository(System.getProperty("user.dir") + "/.git")	
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
