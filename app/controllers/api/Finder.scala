package controllers.api

import modules.AppContext._
import models._
import play.api.mvc._
import play.api.libs.json.Json

object Finder extends Controller {

	def searchRepositories(search: String, page: Int, language: String) = Action {
		Async {
			serviceGithubRepository.search(search, page, language).map(repos =>
				Ok(Json.toJson(repos)))
		}
	}

}