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
	
	def blank(url: String) = Cached("blank") {
		Action {
			Ok(views.html.blank());
		}
	}
	
	def javascriptRoutes = Cached("jsRoutes") {
		Action {
			Ok(Routes.javascriptRouter("jsRoutes")(
				controllers.api.routes.javascript.Finder.searchRepositories,
				controllers.api.routes.javascript.Repository.commits,
				controllers.api.routes.javascript.Repository.contributors,
				controllers.api.routes.javascript.Repository.detail,
				controllers.api.routes.javascript.Repository.languages,
				controllers.api.routes.javascript.Repository.watchers,
				controllers.api.routes.javascript.Stats.impact,
				controllers.api.routes.javascript.Stats.languages,
				controllers.api.routes.javascript.Stats.timeline,
				controllers.api.routes.javascript.User.detail,
				controllers.api.routes.javascript.User.geolocalisation,
				controllers.api.routes.javascript.User.repos
			)).as("text/javascript")
		}
	}
}
