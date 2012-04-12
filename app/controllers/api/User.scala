package controllers.api

import models._
import play.api.mvc._
import play.api.libs.json.Json
import modules.AppContext._
import utils.Query
import play.api.libs.concurrent.Akka
import play.api.Play.current
import play.api.libs.concurrent.Promise

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
				Ok(Json.toJson(repos)))
		}
	}

	def geolocalisation(user: String) = Action {
		Async {
			serviceGithubAuthor.load(user).map { user =>
				if (user.location.isEmpty()) 
					NoContent

				Async {
					serviceYahooWs.findLocation(user.location).map { json =>
						val results = (json \ ("ResultSet") \ ("Found")).as[Int]
						
						if (results < 1) 	
							NoContent
						else 				
							Ok((json \ ("ResultSet") \ ("Results"))(0))
					}
				}
			}
		}
	}

}