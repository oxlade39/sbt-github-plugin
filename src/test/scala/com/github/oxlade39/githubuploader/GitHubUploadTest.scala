package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.apache.commons.httpclient.{HttpMethod, HttpClient}
import org.apache.commons.httpclient.params.HttpMethodParams
import scala.util.parsing.json.JSON
import java.io.File
import org.apache.commons.httpclient.methods.{MultipartPostMethod, PostMethod, PutMethod}
import org.apache.commons.httpclient.methods.multipart.FilePart

class GitHubUploadTest extends JUnit4(GitHubUploadSpec)
object GitHubUploadSpec extends Specification {

  "HttpGitHubUpload" should {

    "upload files to GitHub" in {
      object underTest extends HttpGitHubUpload {
        val httpClient = new HttpClient()
      }

      val upload = new Upload(
        "upload.text",
        "test-%s.txt".format(System.currentTimeMillis), "a test upload")

      val httpClient: HttpClient = new HttpClient()
      val repoConfig = DefaultGitHubConfigFactory()
      val remoteRepository: GitHubRepository = DefaultGitHubConfigFactory.gitHubRepository

      val response = HttpClientHttp.post(remoteRepository.toDownloadURLString, Map[String, Any](
        "file_size" -> upload.fileSize,
        "content_type" -> upload.contentType,
        "file_name" -> upload.name,
        "description" -> upload.description,
        "login" -> repoConfig.login,
        "token" -> repoConfig.token
      ))

      println("Code: %s Response: %s".format(response.code, response.body))
      val responseJSON: Map[String, Any] = JSON.parseFull(response.body).get.asInstanceOf[Map[String, Any]]

      val amazonResponse = HttpClientHttp.post("http://github.s3.amazonaws.com/", Map[String, Any](
        "key" -> (responseJSON("prefix") + upload.name),
        "Filename" -> upload.name,
        "policy" -> responseJSON("policy"),
        "AWSAccessKeyId" -> responseJSON("accesskeyid"),
        "signature" -> responseJSON("signature"),
        "acl" -> responseJSON("acl"),
        "file" -> upload.content,
        "success_action_status" -> 201
      ))
      println("Amazon Response Code: %s Response: %s".format(amazonResponse.code, amazonResponse.body))

      amazonResponse.code mustEqual 201
    }
  }

  trait Http {
    def post(url: String, params: Map[String, Any]): Response
//    def postMultiPart(url: String, params: Map[String, Any]): Response
    def put(url: String, params: Map[String, Any]): Response
  }

  trait Response {
    def code: Int
    def body: String
  }

  object HttpClientHttp extends Http {
    val client: HttpClient = new HttpClient()

    def post(url: String, params: Map[String, Any]) = {
      println("posting: %s".format(params))
      val method: PostMethod = new PostMethod(url)
      execute(method, params)
    }

//    def postMultiPart(url: String, params: Map[String, Any]) = {
//      println("posting: %s".format(params))
//      val method: MultipartPostMethod = new MultipartPostMethod(url)
//      for ((key, value) <- params) {
//        if(key == "file") {
//          method.addPart( new FilePart( "sample.txt", new File(), "text/plain", "ISO-8859-1"))
//        }else {
//
//        }
//      }
//      val responseCode = client.executeMethod(method)
//      new HttpClientReponse(responseCode, method)
//    }

    def put(url: String, params: Map[String, Any]): Response = {
      println("putting: %s".format(params))
      val method: PutMethod = new PutMethod(url)
      execute(method, params)
    }

    def execute(method: HttpMethod, params: Map[String, Any]): Response = {
      val methodParams: HttpMethodParams = new HttpMethodParams()
      for ((key, value) <- params) methodParams.setParameter(key, value.toString)
      method.setParams(methodParams)
      val responseCode = client.executeMethod(method)
      new HttpClientReponse(responseCode, method)
    }

    class HttpClientReponse(_code: Int, method: HttpMethod) extends Response {
      def code = _code
      def body = method.getResponseBodyAsString
    }
  }

}