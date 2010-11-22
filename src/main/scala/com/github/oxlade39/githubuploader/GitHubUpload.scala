package com.github.oxlade39.githubuploader

import java.io.File
import org.apache.commons.httpclient.HttpClient


trait GitHubUpload {
  def upload(request: Upload): Int
}

abstract class HttpGitHubUpload extends GitHubUpload {
  val httpClient: HttpClient

  def upload(request: Upload) = 200
}

sealed case class Upload(
    fileName: File,
    name: String,
    description: String = ""
) {

  def contentType: String = {
//    "application/octet-stream"
    "text/plain"
  }

  lazy val file = new File(fileName)
  lazy val content = io.Source.fromFile(file).getLines.mkString
  lazy val fileSize = file.length
}