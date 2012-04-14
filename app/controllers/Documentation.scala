package controllers

import play.api.mvc._
import play.api.cache.Cached
import play.api.Play.current

object Documentation extends Controller {
	
	// Import implicit list of languages for templates
	import utils.LanguagesFilter.list
	
	def index = Cached("doc") {
		Action {
			Ok(views.html.doc())
		}
	}
}