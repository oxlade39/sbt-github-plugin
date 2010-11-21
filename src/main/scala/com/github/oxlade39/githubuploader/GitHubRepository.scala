package com.github.oxlade39.githubuploader

import java.net.URL


sealed case class GitHubRepository(owner: String, repositoryName: String) {
  def toURL: URL = new URL(toURLString)
  def toURLString: String = "https://github.com/"+owner+"/"+repositoryName
}

object GitHubRepositoryExtractor {
  val PATTERN ="""(git@github.com:|https://\w*@github.com/|git://github.com/)(\w*)/([\w+[._-]]*\w*)\.git""".r

  def apply(url: String): Option[GitHubRepository] = url match {
    case PATTERN(prefix, owner, repoName) => Some(GitHubRepository(owner, repoName))
    case _ => None
  }
}









