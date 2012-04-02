package services

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import utils.Query

class QueryAnalyserSpec extends Specification {

	"QueryBuilder" should {
		
		"access to URL for Github API V2 and V3 from Configuration file" in {
				running(FakeApplication()) {
					Configuration.root().getString("github.api.v2") must contain("github.com")
					Configuration.root().getString("github.api.v3") must contain("github.com")
				}
		}
		"return None if 'from' doesn't exist in the configuration file" in {
			running(FakeApplication()) {
				Query("something").toUrl must beNone
			}
		}
		"alert for incomplete URL if all params are not defined" in {
			running(FakeApplication()) {
				Query("github.query.search_repo").isComplete must beFalse
			}
		}
		"return the complete generated URL when all params are defined" in {
			running(FakeApplication()) {
				val generated = Query("github.query.search_repo").set("query"->"test").set("page"->1).set("lang"->"Scala")
				generated.isComplete must beTrue
				generated.toUrl must beSome[String]
				generated.toUrl.get must endWith("search/test?language=Scala&start_page=1")
			}
		}
	}
}