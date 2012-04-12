package controllers

import play.api.Routes
import play.api.mvc._
import play.api.cache.Cached
import play.api.Play.current
import utils.LanguagesFilter
import play.api.templates.Html

object Application extends Controller {

	implicit val languages : List[Tuple2[String, Seq[String]]] = LanguagesFilter.list
	
	def index() = Cached("homepage") {
		Action {
			Ok(views.html.index())
		}
	}
	
	/**
	 * Return blank template (backbone will fetch the content)
	 */
	def blank(url: String) = Cached("blank") {
		Action {
			Ok(views.html.blank());
		}
	}

}
