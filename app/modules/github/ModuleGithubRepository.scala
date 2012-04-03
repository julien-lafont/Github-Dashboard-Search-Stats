package modules.github

import sindi._
import modules.ws._
import utils.Query
import play.api.libs.concurrent.Promise
import models._
import modules.jsonWs._
import play.api.libs.json.JsValue

final class ModuleGithubRepository(override val ctx: Context) extends Module {
	override lazy val modules = new ModuleJsonWS(this) :: Nil
	override val bindings: Bindings = bind[ServiceGithubRepository] to new ServiceGithubRepositoryWs(from[ModuleJsonWS].service)

	val service = inject[ServiceGithubRepository]
}

trait ServiceGithubRepository {
	val serviceJsonWs: ServiceJsonWS

	def search(search: String, page: Int, language: String): Promise[List[RepositoryV2]]
	def load(user: String, repo: String): Promise[RepositoryV3]
	def listContributors(user: String, repo: String, nb: Int = 30): Promise[List[User]]
	def listWatchers(user: String, repo: String, nb: Int = 30): Promise[List[User]]
	def listCommits(user: String, repo: String, nb: Int = 30): Promise[List[Commit]]
	def listLanguages(user: String, repo: String): Promise[JsValue]
}

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

	override def listCommits(user: String, repo: String, nb: Int = 30) = {
		val query = Query("github.query.list_commits").set("user" -> user).set("repo" -> repo).set("nb", nb)
		println(query.toUrl.get)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[Commit]])
	}

	override def listLanguages(user: String, repo: String) = {
		val query = Query("github.query.list_languages").set("user" -> user).set("repo" -> repo)
		serviceJsonWs.fetchWithCache(query)
	}

}