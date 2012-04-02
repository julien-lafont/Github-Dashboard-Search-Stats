package controllers.api

import models._
import play.api.mvc._
import play.api.libs.json.Json
import modules.AppContext._
import utils.Query

object User extends Controller {

	def detail(user: String) = Action {
		Async {
			serviceGithubAuthor.load(user).map(user => 
				Ok(Json.toJson(user)))
		}
	}
	
	def repos(user: String) = Action {
		Async {
			serviceGithubAuthor.listRepositories(user).map(repos =>
				Ok(Json.toJson(repos))
			)
		}
	}
	
	def geolocalisation(user: String) = Action {
		Async {
			serviceGithubAuthor.load(user).map{ user =>
				if (user.location.length() <= 0) NotFound
				
				Async {
					serviceYahooWs.findLocation(user.location).map{ json =>
						val results:Int = (json\("ResultSet")\("Found")).as[Int]
						if (results < 1)  NotFound
						Ok((json\("ResultSet")\("Results"))(0))
					}
				}
			}
		}
	}
	
}