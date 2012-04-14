package utils
import play.api.libs.json._

object RichJson {
	
	def toJsonIfFound[T](o: Option[T])(implicit tjs: Writes[T]): JsValue = o match {
		case Some(_) => Json.toJson(o)
		case None => noResultJson
	}
	
	val noResultJson = Json.toJson(
		JsObject(Seq(
			"error" -> JsString("No result")	 
		))
	)

}