package controllers

import play.api.Routes
import play.api.mvc._
import play.api.cache.Cached
import play.api.Play.current
import utils.LanguagesFilter

object Documentation extends Controller {

	implicit val languages : List[Tuple2[String, Seq[String]]] = LanguagesFilter.list
	
	def index = Cached("doc") {
		Action {
			Ok(views.html.doc())
		}
	}
}