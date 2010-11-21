package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.eclipse.jgit.storage.file.FileRepository
import java.io.File

class GitHubRepositoryTest extends JUnit4(GitHubRepositorySpec)
object GitHubRepositorySpec extends Specification {

  "GitHubRepository" should {

    "have an owner " in {
      GitHubRepository("owner", null).owner mustEqual "owner"
    }
    "have an owner " in {
      GitHubRepository(null, "repo_name").repositoryName mustEqual "repo_name"
    }	
    "give the URLString equal to the public github URL" in {
      GitHubRepository("owner", "reponame").toURLString mustEqual "https://github.com/owner/reponame"
    }
    "give the URL equal to the public github URL" in {
      GitHubRepository("owner", "reponame").toURL mustEqual new java.net.URL("https://github.com/owner/reponame")
    }
  }
}