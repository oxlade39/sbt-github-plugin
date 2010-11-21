package com.github.oxlade39.githubuploader

import java.io.File
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepository
import java.net.URL


trait GitRepositoryResolver {
  def repository: Repository
}

trait DefaultGitRepositoryResolver extends GitRepositoryResolver {
  def repository = new FileRepository(new File(System.getProperty("user.dir") + "/.git"))
}







