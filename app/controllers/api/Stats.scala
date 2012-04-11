package controllers.api

import models._
import models.Commit.RepositoryTimelineFormat
import play.api.mvc._
import play.api.libs.json.Json
import modules.AppContext._

object Stats extends Controller {

	def languages(user: String) = Action {
		Async {
			serviceStats.extractLanguagesStats(user).map(languages =>
				Ok(Json.toJson(languages)))
		}
	}

	def impact(user: String, repo: String) = Action {
		Async {
			serviceStats.extractUserActivity(user, repo).map(commitsByUser =>
				Ok(Json.toJson(commitsByUser)))
		}
	}

	def timeline(user: String, repo: String) = Action {
		Async {
			serviceStats.extractCommitsInTimeline(user, repo).map(timeline => {
				Ok(Json.toJson(timeline))})
		}
	}

}