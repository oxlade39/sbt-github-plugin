package com.github.oxlade39.githubuploader

import org.apache.commons.httpclient.{HttpMethod, HttpClient}
import org.apache.commons.httpclient.params.HttpMethodParams
import scala.util.parsing.json.JSON
import java.io.File
import org.apache.commons.httpclient.methods.{MultipartPostMethod, PostMethod, PutMethod}
import org.apache.commons.httpclient.methods.multipart._
import scala.collection.mutable.LinkedHashMap

trait Http {
  def post(url: String, params: LinkedHashMap[String, Any]): Response
 	def postMultiPart(url: String, files: List[FilePart], params: LinkedHashMap[String, Any]): Response
  def put(url: String, params: LinkedHashMap[String, Any]): Response
}

trait Response {
  def code: Int
  def body: String
}

object HttpClientHttp extends Http {
  val client: HttpClient = new HttpClient()

  def post(url: String, params: LinkedHashMap[String, Any]) = {
    val method: PostMethod = new PostMethod(url)
    for ((key, value) <- params) method.setParameter(key, value.toString)
    val responseCode = client.executeMethod(method)
    new HttpClientReponse(responseCode, method)
  }

 def postMultiPart(url: String, files: List[FilePart] = Nil, params: LinkedHashMap[String, Any]): Response = {
   val method: PostMethod = new PostMethod(url)
	 var parts: Array[Part] = Array()
   for ((key, value) <- params) {
	    val stringValue: String = 
			if(value.isInstanceOf[String]) value.asInstanceOf[String] 
			else value.toString
		parts = parts :+ new StringPart( key, stringValue)
   }
	 for (f <- files) {
		parts = parts :+ f
	 }
	 method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()))
	
   val responseCode = client.executeMethod(method)
   new HttpClientReponse(responseCode, method)
 }

  def put(url: String, params: LinkedHashMap[String, Any]): Response = {
    val method: PutMethod = new PutMethod(url)
    execute(method, params)
  }

  def execute(method: HttpMethod, params: LinkedHashMap[String, Any]): Response = {
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