package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification

object GitHubRepositoryExtractorSpec extends Specification {

  "GitHubRepositoryExtractor" should {

    "extract a GitHubRepository from the default ssh URL" in {
      GitHubRepositoryExtractor("git@github.com:oxlade39/scala.tmbundle.git") mustEqual Some(GitHubRepository("oxlade39", "scala.tmbundle"))
      GitHubRepositoryExtractor("git@github.com:oxlade39/spring-reference-project.git") mustEqual Some(GitHubRepository("oxlade39", "spring-reference-project"))
    }

    "extract a GitHubRepository from the default HTTP URL" in {
      GitHubRepositoryExtractor("https://github.com/mirrors/linux-2.6.git") mustEqual Some(GitHubRepository("mirrors", "linux-2.6"))
    }

    "extract a GitHubRepository from the private HTTP URL" in {
      GitHubRepositoryExtractor("https://oxlade39@github.com/oxlade39/scala.tmbundle.git") mustEqual Some(GitHubRepository("oxlade39", "scala.tmbundle"))
    }

    "extract a GitHubRepository from the readonly HTTP URL" in {
      GitHubRepositoryExtractor("git://github.com/oxlade39/spring-reference-project.git") mustEqual Some(GitHubRepository("oxlade39", "spring-reference-project"))
      GitHubRepositoryExtractor("git://github.com/Constellation/ruby-net-github-upload.git") mustEqual Some(GitHubRepository("Constellation", "ruby-net-github-upload"))
    }
  }
}