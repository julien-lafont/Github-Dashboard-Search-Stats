package modules.stats

import org.joda.time.LocalDate
import models.Author
import modules.github._
import play.api.libs.concurrent.Promise
import sindi._

final class ModuleExtractData(override val ctx: Context) extends Module {
	override lazy val modules =
		new ModuleGithubAuthor(this) ::
			new ModuleGithubRepository(this) ::
			Nil

	override val bindings: Bindings = bind[ServiceExtractData] to
		new ServiceExtractDataImpl(from[ModuleGithubRepository].service, from[ModuleGithubAuthor].service)

	val service = inject[ServiceExtractData]
}

trait ServiceExtractData {
	val serviceGHRepository: ServiceGithubRepository
	val serviceGHAuthor: ServiceGithubAuthor

	def extractLanguagesStats(user: String): Promise[Map[String, Int]]
	def extractUserActivity(user: String, repo: String): Promise[List[(Author, Int)]]
	def extractCommitsInTimeline(user: String, repo: String): Promise[List[(LocalDate, Int)]]
}