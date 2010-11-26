package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.specs.mock.Mockito
import org.mockito.Matchers._
import scala.collection.mutable.LinkedHashMap

class GitHubUploadTest extends JUnit4(GitHubUploadSpec)
object GitHubUploadSpec extends Specification with Mockito { 

  "HttpGitHubUpload" should {
	
	  val mockHttp = mock[Http]
	  val mockConfig = mock[GitHubConfig]
      object underTest extends HttpGitHubUpload {
        val http = mockHttp
		val repoConfig = mockConfig
      }
	
    "returns response status from post to github if not 200" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns BadResponse(400)

	  val uploadResponse = underTest.upload(new Upload(
        "upload.text",
        "test-%s.txt".format(System.currentTimeMillis), 
		"a test upload"
	  ))
	  uploadResponse mustEqual 400
    }

	"post correctly ordered params to GitHub" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns BadResponse(400)
	  val upload = mock[Upload]
	  upload.fileSize returns 10
	  upload.contentType returns "text/plain"
	  upload.name returns "file.txt"
	  upload.description returns "description"
	  mockConfig.login returns "username"
	  mockConfig.token returns "token_string"
	
	  val uploadResponse = underTest.upload(upload)
	
	  there was one(mockHttp).post("https://github.com/oxlade39/sbt-github-plugin/downloads", LinkedHashMap[String, Any](
        "file_size" -> 10,
        "content_type" -> "text/plain",
        "file_name" -> "file.txt",
        "description" -> "description",
        "login" -> "username",
        "token" -> "token_string"		
	  ))	
	}
  }
}

object BadResponse {
	def apply(_code: Int) = new Response {
		def code: Int = _code
	  	def body: String = ""
	}
}