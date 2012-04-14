package modules.github

import models.Author
import models.RepositoryV3
import modules.jsonWs.ServiceJsonWS
import utils.Query

class ServiceGithubAuthorWs(override val serviceJsonWs: ServiceJsonWS) extends ServiceGithubAuthor {

	/**
	 * Load information about a repository owner
	 */
	override def load(user: String) = {
		val query = Query("github.query.info_user").set("user" -> user)
		serviceJsonWs.fetchModel[Author](query)
	}
	
	/**
	 * Retrieve the repositories created by this user
	 */
	override def listRepositories(user: String, nb: Int = 30) = {
		val query = Query("github.query.list_repositories").set("user" -> user).set("nb", nb)
		serviceJsonWs.fetchModel[List[RepositoryV3]](query)
	}

}