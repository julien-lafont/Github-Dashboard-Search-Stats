package modules.stats

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import models._
import modules.AppTestContext._
import utils.CollectionMerger._
import play.api.libs.concurrent.Promise

class ServiceStatsMockSpec extends Specification {
	
	val user = "studiodev";
	
	"Statistical service [Mock] " should {
	
			"Extract preferred languages of a user" in {
				running(FakeApplication()) {
					val languages = serviceStats.extractLanguagesStats(user).value.get
					languages must haveKey("Scala") haveKey("Java") haveKey("JavaScript")
					languages must havePair("Java", 13740 + 9203);
					languages must havePair("Scala", 1592);
					languages must not haveKey("Cobol") // ;)
				}
			}
			
			"/TODO/ Project commits on a timeline" in {
				true must beTrue
			}
			
			"/TODO/ Extract users activities on a project" in {
				true must beTrue
			}
			
			
	}

}