package models

import play.api.libs.json._

case class Language(language: String, score: Int);

object Language {

	implicit object LanguageFormat extends Format[Language] {
		def reads(json: JsValue) = Language(
			json(0).as[String],
			json(1).as[Int])

		def writes(c: Language): JsValue = JsObject(Seq(
			"lang" 	-> JsString(c.language),
			"score" -> JsNumber(c.score)))
	}

}