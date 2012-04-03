package models

import play.api.libs.json._

case class User(login: String, contributions: Int, url: String, avatar: String);

object User {

	implicit object UserFormat extends Format[User] {
		def reads(json: JsValue) = User(
			(json \ "login").as[String],
			(json \ "contributions").asOpt[Int].getOrElse(-1),
			(json \ "url").as[String],
			(json \ "avatar_url").as[String])

		def writes(c: User): JsValue = JsObject(Seq(
			"login" -> JsString(c.login),
			"contributions" -> JsNumber(c.contributions),
			"url" -> JsString(c.url),
			"avatar" -> JsString(c.avatar)))
	}

}