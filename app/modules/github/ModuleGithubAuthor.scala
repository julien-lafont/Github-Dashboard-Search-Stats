package modules.github

import sindi._
import modules.ws._
import utils.Query
import play.api.libs.concurrent.Promise
import modules.jsonWs.ModuleJsonWS
import models._
import modules.jsonWs.ServiceJsonWS

final class ModuleGithubAuthor(override val ctx: Context) extends Module {
	override lazy val modules = new ModuleJsonWS(this) :: Nil
  override val bindings: Bindings = bind[ServiceGithubAuthor] to new ServiceGithubAuthorWs(from[ModuleJsonWS].service)
	
  val service = inject[ServiceGithubAuthor]
}

trait ServiceGithubAuthor {
	val serviceJsonWs: ServiceJsonWS 
	def load(user: String) : Promise[Author]
	def listRepositories(user: String, nb: Int = 30) : Promise[List[RepositoryV3]]
}

class ServiceGithubAuthorWs(override val serviceJsonWs: ServiceJsonWS) extends ServiceGithubAuthor  {
	
	override def load(user: String) = {
		val query = Query("github.query.info_user").set("user"->user)
		serviceJsonWs.fetchWithCache(query).map(_.as[Author]);
	}
	
	override def listRepositories(user: String, nb: Int = 30) = {
		val query = Query("github.query.list_repositories").set("user"->user).set("nb", nb)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[RepositoryV3]])
	}
	
}