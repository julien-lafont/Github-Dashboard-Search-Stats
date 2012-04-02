package models;

import play.api.libs.json._
import java.util.Date

case class RepositoryV2(name: String, username: String, homepage: String, url: String, watchers: Int, language: String)

object RepositoryV2 {

	implicit object RepositoryV2Format extends Format[RepositoryV2] {
		def reads(json: JsValue) = RepositoryV2(
			(json \ "name").as[String],
			(json \ "username").as[String],
			(json \ "homepage").asOpt[String].getOrElse(""),
			(json \ "url").as[String],
			(json \ "watchers").as[Int],
			(json \ "language").as[String]
		)

		def writes(r: RepositoryV2): JsValue = JsObject(Seq(
			"name" -> JsString(r.name),
			"username"-> JsString(r.username),
			"homepage" -> JsString(r.homepage),
			"url" -> JsString(r.url),
			"watchers" -> JsNumber(r.watchers),
			"language" -> JsString(r.language))
		)
	}

}

