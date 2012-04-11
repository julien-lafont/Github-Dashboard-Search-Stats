package models

import play.api.libs.json._
import java.util.Date
import org.joda.time.DateTime
import org.joda.time.LocalDate

case class Commit(
	url: String,
	author: Author,
	message: String,
	date: DateTime,
	sha: String)

object Commit {

	import models.DateFr.DateFormat

	implicit object CommitFormat extends Format[Commit] {
		def reads(json: JsValue) = Commit(
			(json \ "url").as[String],
			(json \ "author").asOpt[Author].getOrElse(Author("Inconnu", "", "")),
			(json \ "commit" \ "message").as[String],
			(json \ "commit" \ "author" \ "date").as[DateTime],
			(json \ "sha").as[String])

		def writes(c: Commit): JsValue = JsObject(Seq(
			"url" 		-> JsString(c.url),
			"author" 	-> Json.toJson(c.author),
			"message" -> JsString(c.message),
			"date" 		-> Json.toJson(c.date),
			"sha"			-> Json.toJson(c.sha)))
	}
	
	implicit object RepositoryTimelineFormat extends Writes[List[(LocalDate, Int)]] {
		def writes(timeline: List[(LocalDate, Int)]): JsValue = JsArray(
				timeline.map { elem => 
					JsObject(List(
							"date" -> JsString(elem._1.getDayOfMonth()+"/"+elem._1.getMonthOfYear()+"/"+elem._1.getYearOfCentury()), 
							"nb"	 -> JsNumber(elem._2)
					))	
				}
		)
	}

}