package utils

import play.api.Configuration
import play.api.libs.ws._
import play.api.Play

sealed case class Query(val from: String, val params: Map[String, String] = Map()) {

	private val marker = "$"

	def from(from: String): Query = {
		this.copy(from = from)
	}

	def set(elem: (String, Any)): Query = {
		this.copy(params = params + (elem._1 -> elem._2.toString()))
	}

	def toUrl: Option[String] = {
		Play.current.configuration.getString(from) match {
			case Some(base: String) => Some(
				params.foldLeft(base)((url, elem) =>
					url.replace(this.marker + elem._1, elem._2)))
			case _ => None
		}
	}

	def isComplete = {
		!toUrl.getOrElse(marker).contains(marker)
	}
}

object Query {
	def apply(from: String) = new Query(from)
}