package models

import play.api.libs.json._

case class Author(
	login: String,
	url: String,
	avatar: String,
	location: String = "",
	name: String = "")

object Author {

	implicit object AuthorFormat extends Format[Author] {
		def reads(json: JsValue) = Author(
			(json \ "login").as[String],
			(json \ "url").as[String],
			(json \ "avatar_url").as[String],
			(json \ "location").asOpt[String].getOrElse(""),
			(json \ "name").asOpt[String].getOrElse((json \ "login").as[String]))

		def writes(c: Author): JsValue = JsObject(Seq(
			"login" -> JsString(c.login),
			"url" -> JsString(c.url),
			"avatar" -> JsString(c.avatar),
			"location" -> JsString(c.location),
			"name" -> JsString(c.name)))
	}

	implicit object AuthorScoreFormat extends Writes[List[(Author, Int)]] {
		def writes(couples: List[(Author, Int)]): JsValue = JsArray(
			couples.map { elem =>
				JsObject(List("author" -> Json.toJson(elem._1), "score" -> JsNumber(elem._2)))
			})
	}

}