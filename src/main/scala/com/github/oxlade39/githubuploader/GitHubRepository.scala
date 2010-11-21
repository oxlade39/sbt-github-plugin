package com.github.oxlade39.githubuploader

import java.net.URL


sealed case class GitHubRepository(owner: String, repositoryName: String) {
  def toURL: URL = new URL(toURLString)
  def toURLString: String = "https://github.com/"+owner+"/"+repositoryName
}

object GitHubRepositoryExtractor {
  val PATTERN ="""git@github.com:([\w*\d*]+)/([\w*\d*\.]+)\.git""".r

  def apply(url: String): Option[GitHubRepository] = url match {
    case PATTERN(username, gitHubRepoName) => Some(GitHubRepository(username, gitHubRepoName))
    case _ => None
  }
}









