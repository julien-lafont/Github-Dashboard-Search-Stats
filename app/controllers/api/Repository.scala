package controllers.api

import models._
import play.api.mvc._
import utils.RichJson
import modules.AppContext._

object Repository extends Controller {

	def detail(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.load(user, repo).map(repository =>
					Ok(RichJson.toJsonIfFound(repository))
			)
		}
	}

	def commits(user: String, repo: String, nb: Int=30, lastSha: String = "") = Action {
		Async {
			serviceGithubRepository.listCommits(user, repo, nb, lastSha).map(commits =>
				Ok(RichJson.toJsonIfFound(commits)))
		}
	}

	def contributors(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listContributors(user, repo).map(contribs =>
				Ok(RichJson.toJsonIfFound(contribs)))
		}
	}

	def watchers(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listWatchers(user, repo).map(watchers =>
				Ok(RichJson.toJsonIfFound(watchers)))
		}
	}

	def languages(user: String, repo: String) = Action {
		Async {
			serviceGithubRepository.listLanguages(user, repo).map(lang =>
				Ok(RichJson.toJsonIfFound(lang)))
		}
	}

}
