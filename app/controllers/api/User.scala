package controllers.api

import models._
import play.api.mvc._
import modules.AppContext._
import utils.RichJson

object User extends Controller {

	def detail(user: String) = Action {
		Async {
			serviceGithubAuthor.load(user).map(user =>
				Ok(RichJson.toJsonIfFound(user)))
		}
	}

	def repos(user: String) = Action {
		Async {
			serviceGithubAuthor.listRepositories(user).map(repos =>
				Ok(RichJson.toJsonIfFound(repos)))
		}
	}

	def geolocalisation(user: String) = Action {
		Async {
			serviceGithubAuthor.load(user).map { user =>
				
				if (!user.isDefined || user.get.location.isEmpty()) NoContent

				Async {
					serviceYahooWs.findLocation(user.get.location).map { json =>
						val valid = (json \ ("ResultSet") \ ("Found")).as[Int] >= 1
						
						if (!valid) NoContent
						else        Ok((json \ ("ResultSet") \ ("Results"))(0))
					}
				}
				
			}
		}
	}

}