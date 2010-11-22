package com.github.oxlade39.githubuploader

import org.specs.runner.JUnit4
import org.specs.Specification
import org.apache.commons.httpclient.{HttpMethod, HttpClient}
import org.apache.commons.httpclient.methods.PostMethod
import scala.util.parsing.json.JSON
import java.io.File

class GitHubUploadTest extends JUnit4(GitHubUploadSpec)
object GitHubUploadSpec extends Specification {

  "HttpGitHubUpload" should {

    "upload files to GitHub" in {
      object underTest extends HttpGitHubUpload {
        val httpClient = new HttpClient()
      }

      val upload = new Upload(
        "/src/test/resources/upload.text",
        "my_upload.txt", "a test upload")

      val httpClient: HttpClient = new HttpClient()
      val repoConfig = DefaultGitHubConfigFactory()
      val remoteRepository: GitHubRepository = DefaultGitHubConfigFactory.gitHubRepository
      val method: PostMethod = new PostMethod(remoteRepository.toDownloadURLString)

      val response = HttpClientHttp.post(remoteRepository.toDownloadURLString, Map[String, String](
        "file_size" -> upload.fileSize.toString,
        "content_type" -> upload.contentType,
        "file_name" -> upload.name,
        "description" -> upload.description,
        "login" -> repoConfig.login,
        "token" -> repoConfig.token
      ))

      val responseJSON = JSON.parseFull(response.body)
      println("Code: %s Response: %s".format(response.code, responseJSON))

    }
  }

  trait Http {
    def post(url: String, params: Map[String, String]): Response
  }

  trait Response {
    def code: Int
    def body: String
  }

  object HttpClientHttp extends Http {
    val client: HttpClient = new HttpClient()

    def post(url: String, params: Map[String, String]) = {
      val method: PostMethod = new PostMethod(url)
      for ((key, value) <- params) method.setParameter(key, value)

      val responseCode = client.executeMethod(method)
      new HttpClientReponse(responseCode, method)
    }

    class HttpClientReponse(_code: Int, method: HttpMethod) extends Response {
      def code = _code
      def body = method.getResponseBodyAsString
    }
  }

}