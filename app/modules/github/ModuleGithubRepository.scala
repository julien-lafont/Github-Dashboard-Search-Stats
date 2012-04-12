package modules.github

import models._
import modules.jsonWs._
import play.api.libs.concurrent.Promise
import play.api.libs.json.JsValue
import sindi._

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
	def listCommits(user: String, repo: String, nb: Int = 30, lastSha: Option[String] = None): Promise[List[Commit]]
	def listLanguages(user: String, repo: String): Promise[JsValue]
}
