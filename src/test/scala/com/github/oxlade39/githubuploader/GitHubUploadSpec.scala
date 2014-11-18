package com.github.oxlade39.githubuploader

import org.apache.commons.httpclient.methods.multipart._
import org.mockito.Matchers.{eq => isEq}
import org.specs._
import org.specs.mock.Mockito

import scala.collection.mutable.LinkedHashMap

object GitHubUploadSpec extends Specification with Mockito {

  "HttpGitHubUpload" should {

    val mockHttp = mock[Http]
    val mockConfig = mock[GitHubConfig]
    object underTest extends HttpGitHubUpload {
      val http = mockHttp
      val repoConfig = mockConfig
    }

    "return response status from post to github if not 200" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns badResponse(400)

      val uploadResponse = underTest.upload(new Upload(
        testFile,
        "test-%s.txt".format(System.currentTimeMillis),
        "a test upload"
      ))
      uploadResponse mustEqual 400
    }

    "post correct params to GitHub download URL" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns badResponse(402)
      val upload = mock[Upload]
      upload.fileSize returns 10
      upload.contentType returns "text/plain"
      upload.name returns "file.txt"
      upload.description returns "description"
      mockConfig.login returns "username"
      mockConfig.token returns "token_string"

      val uploadResponse = underTest.upload(upload)

      val params: LinkedHashMap[String, Any] = LinkedHashMap[String, Any](
        "file_size" -> 10,
        "content_type" -> "text/plain",
        "file_name" -> "file.txt",
        "description" -> "description",
        "login" -> "username",
        "token" -> "token_string"
      ).asInstanceOf[LinkedHashMap[String, Any]]
      there was one(mockHttp).post("https://github.com/oxlade39/sbt-github-plugin/downloads", params)
    }

    "post correct params to amazon s3 if post to GitHub succeeded" in {
      mockHttp.post(any[String], any[LinkedHashMap[String, Any]]) returns validResponse
      val upload = mock[Upload]
      upload.fileSize returns 10
      upload.contentType returns "text/foo"
      upload.name returns "test.txt"
      upload.description returns "description"
      val file = new java.io.File(getClass.getResource("/response.xml").toURI)
      upload.file returns file

      val files = new FilePart("file", file) :: Nil
      val uploadResponse = underTest.upload(upload)

      val params: LinkedHashMap[String, Any] = LinkedHashMap[String, Any](
        "key" -> "/path",
        "Filename" -> "test.txt",
        "policy" -> "some_policy",
        "AWSAccessKeyId" -> "some_access",
        "Content-Type" -> "text/foo",
        "signature" -> "some_sig",
        "acl" -> "some_acl",
        "success_action_status" -> 201
      ).asInstanceOf[LinkedHashMap[String, Any]]

      there was one(mockHttp).postMultiPart(isEq("http://github.s3.amazonaws.com/"), any[List[FilePart]], isEq(params))
    }
  }

  "real upload" should {
    skip("this will REALLY upload to github")
    "upload to github" in {
      object RealUpload extends HttpGitHubUpload with DefaultGitHubConfigProvider {
        val http = HttpClientHttp
      }
      val response = RealUpload.upload(new Upload(
		testFile,
        "test-%s.txt".format(System.currentTimeMillis),
        "a test upload"
      ))
      response mustEqual 200
    }
  }

  def testFile: java.io.File = new java.io.File(Thread.currentThread.getContextClassLoader.getResource("upload.text").toURI)

  def badResponse(code: Int) = Response(code, () => "")
  def validResponse = Response(200, () => """
      {
        "path" : "/path",
        "policy" : "some_policy",
        "accesskeyid" : "some_access",
        "signature" : "some_sig",
        "acl" : "some_acl"
      }
      """
  )
}
