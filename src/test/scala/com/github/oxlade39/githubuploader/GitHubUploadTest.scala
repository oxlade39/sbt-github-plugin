package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs._
import org.specs.matcher.Matcher
import org.specs.mock.Mockito
import org.mockito.Matchers.{ argThat, anyInt, eq => isEq }
import scala.collection.mutable.LinkedHashMap
import org.apache.commons.httpclient.methods.multipart._

class GitHubUploadTest extends JUnit4(GitHubUploadSpec)
object GitHubUploadSpec extends Specification with Mockito { 

  "HttpGitHubUpload" should {
	
	  val mockHttp = mock[Http]
	  val mockConfig = mock[GitHubConfig]
      object underTest extends HttpGitHubUpload {
        val http = mockHttp
		val repoConfig = mockConfig
      }
	
    "return response status from post to github if not 200" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns BadResponse(400)

	  val uploadResponse = underTest.upload(new Upload(
        "upload.text",
        "test-%s.txt".format(System.currentTimeMillis), 
		"a test upload"
	  ))
	  uploadResponse mustEqual 400
    }

	"post correct params to GitHub download URL" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns BadResponse(402)
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
	
	"post correct params to amazon s3 if post to GitHub succeeded" in {
	  mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns ValidResponse
	  val upload = mock[Upload]
	  upload.fileSize returns 10
	  upload.contentType returns "text/foo"
	  upload.name returns "test.txt"
	  upload.description returns "description"
	  val file = new java.io.File(getClass.getResource("/response.xml").toURI)
	  upload.file returns file
	 
	  val files = new FilePart("file", file) :: Nil
	  val uploadResponse = underTest.upload(upload)
		
 	  there	was one(mockHttp).postMultiPart(isEq("http://github.s3.amazonaws.com/"), haveSize(1), isEq(LinkedHashMap[String, Any](
        "key" -> "/path",
        "Filename" -> "test.txt",
        "policy" -> "some_policy",
        "AWSAccessKeyId" -> "some_access",
        "Content-Type" -> "text/foo",
        "signature" -> "some_sig",
        "acl" -> "some_acl",
        "success_action_status" -> 201
      )))
	}
  }

	//   "real upload" should {
	// "should show" in {
	//   object RealUpload extends HttpGitHubUpload with DefaultGitHubConfigProvider {
	//         val http = HttpClientHttp
	//       }
	//   RealUpload.upload(new Upload(
	//         "upload.text",
	//         "test-%s.txt".format(System.currentTimeMillis), 
	// 	"a test upload"
	//   ))
	// }
	//   }
}

object BadResponse {
	def apply(_code: Int) = new Response {
		def code: Int = _code
	  	def body: String = ""
	}
}

object ValidResponse extends Response {
	def code: Int = 200
  	def body: String = """
		{ 
			"path" : "/path", 
			"policy" : "some_policy", 
			"accesskeyid" : "some_access", 
			"signature" : "some_sig",
			"acl" : "some_acl"
		}   
		"""
}