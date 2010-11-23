package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import scala.util.parsing.json.JSON
import java.io.File
import scala.collection.mutable.LinkedHashMap
import org.apache.commons.httpclient.HttpClient
import org.apache.commons.httpclient.methods.multipart._

class GitHubUploadTest extends JUnit4(GitHubUploadSpec)
object GitHubUploadSpec extends Specification {

  "HttpGitHubUpload" should {

    "upload files to GitHub" in {
      object underTest extends HttpGitHubUpload {
        val httpClient = new HttpClient()
      }

      val upload = new Upload(
        "upload.text",
        "test-%s.txt".format(System.currentTimeMillis), 
		"a test upload"
	  )

      val repoConfig = DefaultGitHubConfigFactory()
      val remoteRepository: GitHubRepository = DefaultGitHubConfigFactory.gitHubRepository

      val response = HttpClientHttp.post(remoteRepository.toDownloadURLString, LinkedHashMap[String, Any](
        "file_size" -> upload.fileSize,
        "content_type" -> upload.contentType,
        "file_name" -> upload.name,
        "description" -> upload.description,
        "login" -> repoConfig.login,
        "token" -> repoConfig.token
      ))

	  response.code mustEqual 200
      val responseJSON: Map[String, Any] = JSON.parseFull(response.body).get.asInstanceOf[Map[String, Any]]

	  val files: List[FilePart] = new FilePart("file", upload.file) :: Nil
      val amazonResponse = HttpClientHttp.postMultiPart("http://github.s3.amazonaws.com/", files,
		LinkedHashMap[String, Any](
        "key" -> responseJSON("path"),
        "Filename" -> upload.name,
        "policy" -> responseJSON("policy"),
        "AWSAccessKeyId" -> responseJSON("accesskeyid"),
        "Content-Type" -> upload.contentType,
        "signature" -> responseJSON("signature"),
        "acl" -> responseJSON("acl"),
        "success_action_status" -> 201
      ))
      println("Amazon Response Code: %s Response: %s".format(amazonResponse.code, amazonResponse.body))

      amazonResponse.code mustEqual 201
    }
  }

}