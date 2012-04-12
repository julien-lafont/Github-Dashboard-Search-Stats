package controllers.api

import modules.AppContext._
import models._
import play.api.mvc._
import play.api.libs.json.Json

object Finder extends Controller {

	def searchRepositories(search: String, page: Int=1, language: Option[String] = None) = Action {
		Async {
			serviceGithubRepository.search(search, page, language.getOrElse("")).map(repos =>
				Ok(Json.toJson(repos)))
		}
	}

}