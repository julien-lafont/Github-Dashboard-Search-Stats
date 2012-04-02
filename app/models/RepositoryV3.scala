package models

import play.api.libs.json._
import java.util.Date

case class RepositoryV3(
	name: String, homepage: String, url: String, watchers: Int, 
	language: String, createdAt: String, description: String,
	owner: Author);

object RepositoryV3 {
	
	implicit object RepositoryV3Format extends Format[RepositoryV3] {
		def reads(json: JsValue) = RepositoryV3(
			(json \ "name").as[String],
			(json \ "homepage").asOpt[String].getOrElse(""),
			(json \ "url").as[String],
			(json \ "watchers").as[Int],
			(json \ "language").asOpt[String].getOrElse(""),
			(json \ "created_at").asOpt[String].getOrElse(""),
			(json \ "description").asOpt[String].getOrElse(""),
			(json \ "owner").as[Author])
		
		def writes(r: RepositoryV3): JsValue = JsObject(Seq(
			"name" -> JsString(r.name),
			"homepage" -> JsString(r.homepage),
			"url" -> JsString(r.url),
			"watchers" -> JsNumber(r.watchers),
			"language" -> JsString(r.language),
			"createdAt" -> JsString(r.createdAt),
			"description" -> JsString(r.description),
			"owner"	-> Json.toJson(r.owner)
		))
	}

}