package com.github.oxlade39.githubuploader

import java.io.File
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository
import java.net.URL


sealed case class GitHubRepository(owner: String, repositoryName: String) {
  def toURL: URL = new URL(toURLString)
  def toURLString: String = "https://github.com/"+owner+"/"+repositoryName
}









