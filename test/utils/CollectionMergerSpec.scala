package utils

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.Configuration
import utils.CollectionMerger._
import org.specs2.matcher.BeEqualTo

class CollectionMergerSpec extends Specification {
		
	"[TU] Collection merger" should {
		
		val origin = Map("a" -> 3, "b" -> 6)
		val list = List(Map("a"->1), Map("b"->2, "c"->3), Map("c"->2))
	
		"Merge a list of map and calculate sum of values" in {
			
			"Merge a single map" in {
				mergeMap(List(origin))(_+_) must be_===(origin)
			}
			
			"Merge N times the same map" in {
				val merged = mergeMap(List(origin, origin))(_+_)
				merged("a") must beEqualTo(origin("a")*2)
				merged("b") must beEqualTo(origin("b")*2)
			}
			
			"Merge N times the same map with * operation" in {
				val merged = mergeMap(List(origin, origin))(_*_)
				merged("a") must beEqualTo(origin("a")*origin("a"))
				merged("b") must beEqualTo(origin("b")*origin("b"))
			}
			
			"Merge maps with differents items" in {
				val merged = mergeMap(list)(_+_)
				merged must havePair("a" -> 1)
				merged must havePair("b" -> 2)
				merged must havePair("c" -> 5)
			}
		}
		
		"Merge a list of map and count the elements" in {
			
			val l1 = List("a")
			val l2 = List("a", "a")
			val l3 = List("a", "a", "b", "c", "a", "b")
			
			"Count elements in a 1-element list" in {
				mergeAndCountList(l1)("a") must beEqualTo(1)
			}
			
			"Count elements in a n-elements list" in {
				mergeAndCountList(l2) must havePair("a" -> 2)
			}
			
			"Count elements in a n-elements list with differents elements" in {
				mergeAndCountList(l3) must havePair("a" -> 3) havePair("b" -> 2) havePair("c" -> 1)
			}
		}
	}
}