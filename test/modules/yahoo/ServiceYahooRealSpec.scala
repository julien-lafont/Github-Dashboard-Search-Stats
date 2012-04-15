package modules.yahoo

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import models._
import modules.AppContext._

class ServiceYahooMockSpec extends Specification {

	"Yahoo Geoposition service" should {
		
		"Find country and region from a city" in {
			running(FakeApplication()) {
				val json = serviceYahooWs.findLocation("Montpellier").value.get.toString()
				
				json must */("Found" -> 1)
				json must */("country" -> "France")
				json must */("state" -> "Languedoc-Roussillon")
				json must */("countrycode" -> "FR")
			}
		}
		
		"Find gps coordinates from a city" in {
			running(FakeApplication()) {
				val json = serviceYahooWs.findLocation("Montpellier").value.get.toString()
				json must */("latitude" -> "43.610855")
				json must */("longitude" -> "3.876090")
			}
		}
		
		"Find country from a complex address" in {
			running(FakeApplication()) {
				val json = serviceYahooWs.findLocation("30 rue des peupliers, Bat F apt 23, 34090 Montpellier France").value.get.toString()
				json must */("Found" -> 1)
				json must */("country" -> "France")
				json must */("city" -> "Montpellier")
				json must */("postal" -> "34090")
			}
		}
	
	}
	
		
}