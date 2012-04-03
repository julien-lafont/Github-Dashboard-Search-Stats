package controllers

import play.api._
import play.api.mvc._
import play.api.libs.ws._
import play.api.libs.json._
import models._

object Application extends Controller {

	def index = Action {
		Async {

			WS.url("https://api.github.com/repos/studiodev/Play20/commits").get().map { response =>
				println("hi")
				val commits: List[Commit] = response.json.as[List[Commit]]
				println(commits);
				println(response.header("X-RateLimit-Remaining").getOrElse("inconnu")+"/"+response.header("X-RateLimit-Limit").getOrElse("inconnu"))
				Ok(response.json)
			}
		}

	}

}
