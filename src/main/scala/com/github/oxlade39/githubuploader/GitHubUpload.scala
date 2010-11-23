package com.github.oxlade39.githubuploader

import java.io.File
import org.apache.commons.httpclient.HttpClient
import java.net.{URI, URL}

trait GitHubUpload {
  def upload(request: Upload): Int
}

abstract class HttpGitHubUpload extends GitHubUpload {
  val httpClient: HttpClient

  def upload(request: Upload) = 200
}

sealed case class Upload(
    fileName: String,
    name: String,
    description: String = ""
) {

  def contentType: String = {
//    "application/octet-stream"
    "text/plain"
  }

  private def checkFile = {
    val uri: URI = Thread.currentThread.getContextClassLoader.getResource(fileName).toURI
    val f = new File(uri)
    if(f.exists == false || f.canRead == false) throw new RuntimeException("%s doesn't exist or isn't readable".format(fileName))
    f
  }

  val file = checkFile
  lazy val content = io.Source.fromFile(file).getLines.mkString
  lazy val fileSize = file.length
}