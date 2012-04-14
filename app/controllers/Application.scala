package controllers

import play.api.Routes
import play.api.mvc._
import play.api.cache.Cached
import play.api.Play.current
import play.api.templates.Html

object Application extends Controller {

	// Import implicit list of languages for templates
	import utils.LanguagesFilter.list

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
