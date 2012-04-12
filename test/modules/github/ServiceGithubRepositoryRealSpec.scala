package modules.github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import models._
import modules.AppContext._
import play.api.libs.json.JsValue
import play.api.libs.json.Json

class ServiceGithubRepositoryRealSpec extends Specification {

	"Github Real Repository service" should {
		
			"Search repositories by keywords" in {
				running(FakeApplication()) {
					val repos = serviceGithubRepository.search("play", 1, "").value.get
					repos.length must beGreaterThan(0)
					repos.filter(_.name == "Play20").length must beGreaterThan(0)
				}
			}
			
			"Search repositories by keywords and language" in {
				running(FakeApplication()) {
					val repos = serviceGithubRepository.search("play", 1, "scala").value.get
					repos.length must beGreaterThan(0)
					repos.filter(_.name == "Play20").length must beGreaterThan(0)
					repos.filter(_.name == "Play").length must beEqualTo(0)
				}
			}
			
			"Search repositories by keyword and language for a given page" in {
				running(FakeApplication()) {
					val repos = serviceGithubRepository.search("play", 2, "ruby").value.get
					repos.length must beGreaterThan(0)
					repos.filter(_.name == "Play20").length must beEqualTo(0)
				}
			}
			
			"Fetch repository detail" in {
				running(FakeApplication()) {
					val repo = serviceGithubRepository.load("playframework", "play20").value.get
					repo.name must beEqualTo("Play20")
					repo.homepage must beEqualTo("http://www.playframework.org/2.0")
					repo.owner.login must beEqualTo("playframework")
				}
			}
			
			"Fetch contributors list" in {
				running(FakeApplication()) {
					val contribs = serviceGithubRepository.listContributors("playframework", "play20").value.get
					contribs.length must beGreaterThan(20)
					contribs.find(_.login=="guillaumebort") must beSome
					contribs.find(_.login=="guillaumebort").get.contributions must beGreaterThan(100)
				}
			}
			
			"Fetch watchers list" in {
				running(FakeApplication()) {
					val watchers = serviceGithubRepository.listWatchers("playframework", "play20").value.get
					watchers.length must beGreaterThan(0)
					watchers.head.login must not be empty 
				}
			}
			
			"Fetch commits list" in {
				running(FakeApplication()) {
					val commits = serviceGithubRepository.listCommits("playframework", "play20").value.get
					commits.length must beGreaterThan(0)
					commits.head.author.name must not be empty
				}
			}
			
			"Fetch languages" in {
				running(FakeApplication()) {
					val languages : JsValue = serviceGithubRepository.listLanguages("playframework", "play20").value.get
					languages toString() must contain("Scala")
					languages toString() must contain("Java")
				}
			}
	}
	
	"Repository JSON marshalling" should {
			
			"Return a unique key :user-:user" in {
				running(FakeApplication()) {
					val json =  Json.toJson(serviceGithubRepository.load("playframework", "play20").value.get).toString()
					json must /("key" -> "playframework/Play20")
				}
			}
			
			"Return date in french format" in {
				running(FakeApplication()) {
					val json =  Json.toJson(serviceGithubRepository.load("playframework", "play20").value.get).toString()
					json must /("createdAt" -> "7 sept. 2011 11:24:08")
				}
			}
			
			"Return the owner information" in {
				running(FakeApplication()) {
					val json =  Json.toJson(serviceGithubRepository.load("playframework", "play20").value.get).toString()
					json must */("login" -> "playframework")
					json must */("url" -> "https://api.github.com/users/playframework")
					json must */("location" -> "")
				}
			}
				
	}
	
}