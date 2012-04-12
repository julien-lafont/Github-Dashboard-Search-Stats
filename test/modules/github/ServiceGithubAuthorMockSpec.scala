package modules.github

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import models._
import modules.AppTestContext._

class ServiceGithubAuthorMockSpec extends Specification {

	val user = "studiodev"
		
	"[TI Mock] Github Author service" should {
		
		"fetch user details" in {
			running(FakeApplication()) {
				val author = serviceGithubAuthor.load(user).value.get
				(author.login) must beEqualTo(user)
				(author.location) must beEqualTo("Montpellier")
				(author.name) must beEqualTo("Julien Lafont")
			}
		}
		
		"fetch user repositories" in {
			running(FakeApplication()) {
				val repos = serviceGithubAuthor.listRepositories(user).value.get
				(repos.size) must beGreaterThan(0)
				repos(0).owner.login must beEqualTo(user)
			}
		}
		
	}
	
}