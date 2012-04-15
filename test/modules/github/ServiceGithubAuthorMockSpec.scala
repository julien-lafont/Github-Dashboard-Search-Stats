package modules.github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import models._

class ServiceGithubAuthorMockSpec extends Specification {

	val user = "studiodev"
		
	"Github Author service [Mock]" should {
		
		import modules.AppTestContext._ // MOCK WS

		"fetch user details" in {
			running(FakeApplication()) {
				val author = serviceGithubAuthor.load(user).value.get.getOrElse(failure("No user"))
				(author.login) must beEqualTo(user)
				(author.location) must beEqualTo("Montpellier")
				(author.name) must beEqualTo("Julien Lafont")
			}
		}
		
		"fetch user repositories" in {
			running(FakeApplication()) {
				val repos = serviceGithubAuthor.listRepositories(user).value.get.getOrElse(failure("No user"))
				(repos.size) must beGreaterThan(0)
				repos(0).owner.login must beEqualTo(user)
			}
		}
	}
	
	"Github Author service" should {
		
		import modules.AppContext._	
		
		"throw an error if the author doesn't exist" in {
			running(FakeApplication()) {
				val repos = serviceGithubAuthor.listRepositories("").value.get
				repos must beNone
			}
		}
		
	}
	
}