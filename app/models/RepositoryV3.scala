package models

import play.api.libs.json._
import java.util.Date
import java.text.SimpleDateFormat
import scala.util.control.Exception.catching
import java.text.ParseException
import org.joda.time.DateTime

case class RepositoryV3(
	name: String, homepage: String, url: String, watchers: Int,
	forks: Int, issues: Int, isFork: Boolean, isPrivate: Boolean,
	language: String, createdAt: DateTime, description: String,
	owner: Author);

object RepositoryV3 {

	import models.DateFr.DateFormat

	implicit object RepositoryV3Format extends Format[RepositoryV3] {
		def reads(json: JsValue) = RepositoryV3(
			(json \ "name").as[String],
			(json \ "homepage").asOpt[String].getOrElse(""),
			(json \ "url").as[String],
			(json \ "watchers").as[Int],
			(json \ "forks").as[Int],
			(json \ "open_issues").as[Int],
			(json \ "fork").as[Boolean],
			(json \ "private").as[Boolean],
			(json \ "language").asOpt[String].getOrElse(""),
			(json \ "created_at").as[DateTime],
			(json \ "description").asOpt[String].getOrElse(""),
			(json \ "owner").as[Author])

		def writes(r: RepositoryV3): JsValue = JsObject(Seq(
			"name" -> JsString(r.name),
			"homepage" -> JsString(r.homepage),
			"url" -> JsString(r.url),
			"watchers" -> JsNumber(r.watchers),
			"forks" -> JsNumber(r.forks),
			"is_fork" -> JsBoolean(r.isFork),
			"is_private" -> JsBoolean(r.isPrivate),
			"issues" -> JsNumber(r.issues),
			"language" -> JsString(r.language),
			"createdAt" -> Json.toJson(r.createdAt),
			"description" -> JsString(r.description),
			"owner" -> Json.toJson(r.owner),
			"key" -> JsString(r.owner.login+"/"+r.name)))
	}

}

