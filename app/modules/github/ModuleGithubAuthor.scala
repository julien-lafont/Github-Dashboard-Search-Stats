package modules.github

import models._
import modules.jsonWs._
import play.api.libs.concurrent.Promise
import sindi._

final class ModuleGithubAuthor(override val ctx: Context) extends Module {
	override lazy val modules = new ModuleJsonWS(this) :: Nil
	override val bindings: Bindings = bind[ServiceGithubAuthor] to new ServiceGithubAuthorWs(from[ModuleJsonWS].service)

	val service = inject[ServiceGithubAuthor]
}

trait ServiceGithubAuthor {
	val serviceJsonWs: ServiceJsonWS
	def load(user: String): Promise[Author]
	def listRepositories(user: String, nb: Int = 30): Promise[List[RepositoryV3]]
}

