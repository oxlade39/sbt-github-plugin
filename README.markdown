sbt-github-plugin
====================

SBT GitHub tasks

Supported Tasks:
-----------------

1. Upload SBT artifacts to GitHub

Installing Plugin
-----------------
Add the following to project/plugins/Plugins.scala:
<pre><code>
	import sbt._
	class Plugins(info: ProjectInfo) extends PluginDefinition(info)
	{
	  val sbtGitHubPlugin = "com.github.oxlade39" % "sbt-github-plugin" % "1.0"
	}	
</code></pre>
---
## 1. Upload SBT artifacts to GitHub

### Plugin Class: 
*com.github.oxlade39.githubuploader.GitHubUploadedArtifacts*

### Example Usage:
<pre><code>
	import sbt._
	import com.github.oxlade39.githubuploader.GitHubUploadedArtifacts
	class MyProject(info: ProjectInfo) extends DefaultProject(info) with GitHubUploadedArtifacts {
		...
	}
</code></pre>
*reload* and *update* in SBT, then you can use the task *git-hub-upload*

### It does what?
By default *git-hub-upload* will upload all of your SBT defined [artifacts](http://code.google.com/p/simple-build-tool/wiki/Artifacts) to your remote GitHub download page.

*sbt-github-plugin* eats its own dog food and uses the *GitHubUploadedArtifacts* *trait* to upload artifacts to the [downloads page](https://github.com/oxlade39/sbt-github-plugin/downloads). You can see the definition [here](https://github.com/oxlade39/sbt-github-plugin/blob/master/project/build/SBTGitHubPluginProject.scala)

### Configuration
*git-hub-upload* assumes some default behaviour which is standard GitHub practice. It uses [JGit](http://www.jgit.org/) to work out your GitHub remote repository, username and GitHub token. All required to upload to GitHub.

The default assumptions are:

* Your project is set up already tracking your remote GitHub repository
* You have set up git with your GitHub token as described [here](http://help.github.com/git-email-settings/)
* Your GitHub repository is being tracked as *origin*

All of these assumptions are configurable:
<pre><code>
import sbt._
import com.github.oxlade39.githubuploader.GitHubUploadedArtifacts
import com.github.oxlade39.githubuploader.SbtGitHubPluginConfig

class SBTGitHubPluginProject(info: ProjectInfo) extends DefaultProject(info) with GitHubUploadedArtifacts {

 override def customConfiguration: Option[SbtGitHubPluginConfig] = 
				Some(SbtGitHubPluginConfig("github", "remote", "remoteRefName"))
}
</code></pre>

---