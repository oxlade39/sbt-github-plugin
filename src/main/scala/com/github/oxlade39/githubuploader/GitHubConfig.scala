package com.github.oxlade39.githubuploader

import java.io.File
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository
import java.net.URL


sealed case class GitHubConfig(login: String, token: String, repositoryName: String)

abstract class GitHubConfigFactory {
  def repository: Repository

  def apply(): GitHubConfig = {
    new GitHubConfig(login(repository), token(repository), repoName(repository))
  }

  def login(repo: Repository): String = repo.getConfig.getString("github", null, "user")
  def token(repo: Repository): String = repo.getConfig.getString("github", null, "token")
  def repoName(repo: Repository): String =
    parseRemoteUrl(repo.getConfig.getString("remote", "origin", "url")).getOrElse {
      throw new RuntimeException("Couldn't determine url")
    }

  def parseRemoteUrl(url: String): Option[String] = {
    val PATTERN ="""git@github.com:([\w*\d*]+)/([\w*\d*]+)\.git""".r
    url match {
      case PATTERN(username, gitHubRepoName) => Some(GitHubRepository(username, gitHubRepoName).toURLString)
      case _ => None
    }
  }
}

trait GitRepositoryResolver {
  def repository: Repository
}

trait DefaultGitRepositoryResolver extends GitRepositoryResolver {
  def repository = new FileRepository(new File(System.getProperty("user.dir") + "/.git"))
}

sealed case class GitHubFile(localFile: File, description: String)

