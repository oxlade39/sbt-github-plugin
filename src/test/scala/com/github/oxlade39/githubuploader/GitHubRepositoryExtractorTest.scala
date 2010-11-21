package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.eclipse.jgit.storage.file.FileRepository
import java.io.File

class GitHubRepositoryExtractorTest extends JUnit4(GitHubRepositoryExtractorSpec)
object GitHubRepositoryExtractorSpec extends Specification {

  "GitHubRepositoryExtractor" should {

    "extract a GitHubRepository from the default ssh URL" in {
      val sshURL = "git@github.com:oxlade39/scala.tmbundle.git"
      GitHubRepositoryExtractor(sshURL) mustEqual Some(GitHubRepository("oxlade39", "scala.tmbundle"))
    }
  }
}