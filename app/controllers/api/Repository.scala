package controllers.api

import models._
import play.api.mvc._
import play.api.libs.json.Json
import modules.AppContext._

object Repository extends Controller {
	
	def detail(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.load(user, repo).map(repo => 
				Ok(Json.toJson(repo)))
		}	
	}
	
	def commits(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listCommits(user, repo).map(commits =>
				Ok(Json.toJson(commits))
			)
		}
	}
	
	def contributors(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listContributors(user, repo).map(contribs =>
				Ok(Json.toJson(contribs))
			)
		}
	}
	
	def watchers(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listWatchers(user, repo).map(watchers =>
				Ok(Json.toJson(watchers))
			)
		}
	}
	
	def languages(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listLanguages(user, repo).map(
				Ok(_)
			)
		}
	}
	
}
