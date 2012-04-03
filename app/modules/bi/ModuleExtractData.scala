package modules.bi

import sindi._
import modules.github._
import play.api.libs.concurrent.Promise
import models._
import utils.CollectionMerger._

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
	def extractCommitsInTimeline(user: String, repo: String): Promise[List[Commit]]
}

class ServiceExtractDataImpl(override val serviceGHRepository: ServiceGithubRepository,
	override val serviceGHAuthor: ServiceGithubAuthor) extends ServiceExtractData {

	def extractCommitsInTimeline(user: String, repo: String) = {
		for {
			commits <- serviceGHRepository.listCommits(user, repo, 100)
		} yield {
			commits.sortWith((recent, old) => recent.date.compareTo(old.date) < 0)
		}
	}

	def extractUserActivity(user: String, repo: String) = {
		for {
			commits <- serviceGHRepository.listCommits(user, repo, 100)
			result = commits.map(_.author)
		} yield {
			mergeAndCountList(result).toList.sortWith(_._2 > _._2)
		}
	}

	def extractLanguagesStats(user: String) = {
		for {
			repos <- serviceGHAuthor.listRepositories(user)
			result <- Promise.sequence(repos.map(repo =>
				for {
					listL <- serviceGHRepository.listLanguages(user, repo.name)
					languages = listL.as[Map[String, Int]]
				} yield languages))
		} yield {
			mergeMap(result.toList)(_ + _)
		}
	}
}