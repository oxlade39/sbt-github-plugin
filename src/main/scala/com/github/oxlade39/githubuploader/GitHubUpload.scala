package com.github.oxlade39.githubuploader

import java.io.File
import org.apache.commons.httpclient.HttpClient
import java.net.{URI, URL}
import scala.util.parsing.json.JSON
import scala.collection.mutable.LinkedHashMap
import org.apache.commons.httpclient.methods.multipart._

trait GitHubUpload {
  def upload(request: Upload): Int
}

abstract class HttpGitHubUpload extends GitHubUpload with GitHubConfigProvider {
  val http: Http
  val remoteRepository: GitHubRepository = DefaultGitHubConfigFactory.gitHubRepository
  val gitHubAmazonS3URL = "http://github.s3.amazonaws.com/"

  def upload(request: Upload): Int = {
      val response = http.post(remoteRepository.toDownloadURLString, params(
        "file_size" -> request.fileSize,
        "content_type" -> request.contentType,
        "file_name" -> request.name,
        "description" -> request.description,
        "login" -> repoConfig.login,
        "token" -> repoConfig.token
      ))
	  // println(response.body)
	  if(response.code != 200) response.code
	  else continueWithPostToAmazon(request, response)
  }

  def continueWithPostToAmazon(request: Upload, response: Response): Int = {
      val responseJSON: Map[String, Any] = JSON.parseFull(response.body()).get.asInstanceOf[Map[String, Any]]

	  val files: List[FilePart] = new FilePart("file", request.file) :: Nil
      val amazonResponse = http.postMultiPart(gitHubAmazonS3URL, files, params(
        "key" -> responseJSON("path"),
        "Filename" -> request.name,
        "policy" -> responseJSON("policy"),
        "AWSAccessKeyId" -> responseJSON("accesskeyid"),
        "Content-Type" -> request.contentType,
        "signature" -> responseJSON("signature"),
        "acl" -> responseJSON("acl"),
        "success_action_status" -> 201
      ))

      sanatise(amazonResponse)
  }

  def sanatise(response: Response): Int = if(response == null) 500 else
    response match {
      case Response(201, _) => 200
      case Response(other, _) => other
    }

  def params[A, B](elems: (A, B)*): LinkedHashMap[A, B] = {
    LinkedHashMap[A, B](elems:_*).asInstanceOf[LinkedHashMap[A, B]]
  }
}

sealed case class Upload(
    file: java.io.File,
    name: String,
    description: String
) {

  def contentType: String = {
//    "application/octet-stream"
    "text/plain"
  }

  lazy val content = io.Source.fromFile(file).getLines.mkString
  lazy val fileSize = file.length
}