package modules.github

import models._
import modules.jsonWs.ServiceJsonWS
import utils.Query
import play.api.libs.json._

class ServiceGithubRepositoryWs(override val serviceJsonWs: ServiceJsonWS) extends ServiceGithubRepository {

	/**
	 * Search all repositories which match with the query
	 */
	override def search(search: String, page: Int, language: String) = {
		val query = Query("github.query.search_repo").set("query" -> search).set("page" -> page).set("lang" -> language)
		serviceJsonWs.fetchWithCache(query).map(json => (json \ "repositories").as[List[RepositoryV2]])
	}

	/**
	 * Load information about a repository
	 */
	override def load(user: String, repo: String) = {
		val query = Query("github.query.info_repo").set("user" -> user).set("repo" -> repo)
		serviceJsonWs.fetchModel[RepositoryV3](query);
	}

	/**
	 * Return all the contributors (= commiters) of the repository
	 */
	override def listContributors(user: String, repo: String, nb: Int = 30) = {
		val query = Query("github.query.list_contributors").set("user" -> user).set("repo" -> repo).set("nb", nb)
		serviceJsonWs.fetchModel[List[User]](query);
	}

	/**
	 * Return all the watchers of the repository
	 */
	override def listWatchers(user: String, repo: String, nb: Int = 30) = {
		val query = Query("github.query.list_watchers").set("user" -> user).set("repo" -> repo).set("nb", nb)
		serviceJsonWs.fetchModel[List[User]](query)
	}

	/**
	 * Return the last commits in the repository
	 */
	override def listCommits(user: String, repo: String, nb: Int = 30, lastSha: String = "") = {
		val query = Query("github.query.list_commits").set("user" -> user).set("repo" -> repo).set("nb" -> nb).set("sha" -> lastSha)
		serviceJsonWs.fetchModel[List[Commit]](query)
	}

	/**
	 * Return a list of the languages used in the repository
	 */
	override def listLanguages(user: String, repo: String) = {
		val query = Query("github.query.list_languages").set("user" -> user).set("repo" -> repo)
		serviceJsonWs.fetchModel[JsObject](query)
	}

}