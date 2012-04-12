package modules.github

import models.Author
import models.RepositoryV3
import modules.jsonWs.ServiceJsonWS
import utils.Query

class ServiceGithubAuthorWs(override val serviceJsonWs: ServiceJsonWS) extends ServiceGithubAuthor {

	override def load(user: String) = {
		val query = Query("github.query.info_user").set("user" -> user)
		serviceJsonWs.fetchWithCache(query).map(_.as[Author]);
	}

	override def listRepositories(user: String, nb: Int = 30) = {
		val query = Query("github.query.list_repositories").set("user" -> user).set("nb", nb)
		serviceJsonWs.fetchWithCache(query).map(_.as[List[RepositoryV3]])
	}

}