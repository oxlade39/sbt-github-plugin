package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import java.io.File

class DefaultGitRepositoryResolverTest extends JUnit4(DefaultGitRepositoryResolverSpec)
object DefaultGitRepositoryResolverSpec extends Specification {

  "DefaultGitRepositoryResolver" should {

    "resolve the Repository from the current working directory" in {
      val currentWorkingDir = System.getProperty("user.dir")
      object underTest extends DefaultGitRepositoryResolver
      underTest.repository.getDirectory mustEqual new File(currentWorkingDir + "/.git")
    }
  }
}