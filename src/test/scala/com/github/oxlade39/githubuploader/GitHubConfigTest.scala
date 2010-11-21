package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.eclipse.jgit.storage.file.FileRepository
import java.io.File

class GitHubConfigTest extends JUnit4(GitHubConfigSpec)
object GitHubConfigSpec extends Specification {

  "GitHubConfigFactory" should {

    "create GitHubConfig using git repository" in {
      object TestGitHubConfigFactory extends GitHubConfigFactory {
        def repository = new FileRepository(new File("/Users/danoxlade/proj/scala/scalaflow" + "/.git"))
      }

      println(TestGitHubConfigFactory.apply)
      TestGitHubConfigFactory.apply must notBeNull
      TestGitHubConfigFactory.apply.login mustEqual "oxlade39"
    }
  }
}

