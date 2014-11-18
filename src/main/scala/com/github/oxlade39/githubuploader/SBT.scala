package com.github.oxlade39.githubuploader

object uploader extends HttpGitHubUpload with DefaultGitHubConfigProvider {
	val http = HttpClientHttp
}