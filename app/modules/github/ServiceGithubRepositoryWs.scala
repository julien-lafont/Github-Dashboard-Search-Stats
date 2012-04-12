package modules.github

import models._
import modules.jsonWs.ServiceJsonWS
import utils.Query

class ServiceGithubRepositoryWs(override val serviceJsonWs: ServiceJsonWS) extends ServiceGithubRepository {

	override def search(search: String, page: Int, language: String) = {
		val query = Query("github.query.search_repo").set("query" -> search).set("page" -> page).set("lang" -> language)
		serviceJsonWs.fetchWithCache(query).map(json => (json \ "repositories").as[List[RepositoryV2]])
	}

	override def load(user: String, repo: String) = {
		val query = Query("github.query.info_repo").set("user" -> user).set("repo" -> repo)
		serviceJsonWs.fetchWithCache(query).map(_.as[RepositoryV3])
	}

	override def listContributors(user: String, repo: String, nb: Int = 30) = {
		val query = Query("github.query.list_contributors").set("user" -> user).set("repo" -> repo).set("nb", nb)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[User]])
	}

	override def listWatchers(user: String, repo: String, nb: Int = 30) = {
		val query = Query("github.query.list_watchers").set("user" -> user).set("repo" -> repo).set("nb", nb)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[User]])
	}

	override def listCommits(user: String, repo: String, nb: Int = 30, lastSha: Option[String] = None) = {
		val query = Query("github.query.list_commits").set("user" -> user).set("repo" -> repo).set("nb" -> nb).set("sha" -> lastSha.getOrElse(""))
		println(query.toUrl.get)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[Commit]])
	}

	override def listLanguages(user: String, repo: String) = {
		val query = Query("github.query.list_languages").set("user" -> user).set("repo" -> repo)
		serviceJsonWs.fetchWithCache(query)
	}

}