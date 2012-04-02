package models

import play.api.libs.json._

case class Commit(
		url: String, 
		sha: String, 
		author: Author
)

object Commit {

  implicit object CommitFormat extends Format[Commit] {
	  def reads(json: JsValue) = Commit(
	    (json \ "url").as[String],
	    (json \ "sha").as[String],
	    (json \ "author").as[Author]
	  )
	
	  def writes(c: Commit): JsValue = JsObject(Seq(
	      "url" -> JsString(c.url),
	      "sha" -> JsString(c.sha),
	      "author" -> Json.toJson(c.author)
	  ))  
  }

}