package models

import play.api.libs.json._
import java.util.Date
import org.joda.time.DateTime

case class Commit(
		url: String, 
		author: Author,
		message: String,
		date: DateTime
)

object Commit {

	import models.DateFr.DateFormat
	
  implicit object CommitFormat extends Format[Commit] {
	  def reads(json: JsValue) = Commit(
	    (json \ "url").as[String],
	    (json \ "author").asOpt[Author].getOrElse(Author("Inconnu", "", "")),
	    (json \ "commit" \ "message").as[String],
	    (json \ "commit" \ "author" \ "date").as[DateTime]	    
	  )
	
	  def writes(c: Commit): JsValue = JsObject(Seq(
	      "url" -> JsString(c.url),
	      "author" -> Json.toJson(c.author),
	      "message" -> JsString(c.message),
	      "date" -> Json.toJson(c.date)
	  ))  
  }

}