package modules.bi

import sindi._
import modules.github._
import play.api.libs.concurrent.Promise
import models._
import utils.CollectionMerger._
import org.joda.time.LocalDate

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

class ServiceExtractDataImpl(override val serviceGHRepository: ServiceGithubRepository,
	override val serviceGHAuthor: ServiceGithubAuthor) extends ServiceExtractData {

	/**
	 * Generate timeline from commits (= the number of commits for each day )
	 */
	def extractCommitsInTimeline(user: String, repo: String) = {
		for {
			commits <- serviceGHRepository.listCommits(user, repo, 100)
		} yield {
			commits.groupBy(e => e.date.toLocalDate()).map(elem => elem._1 -> elem._2.size).toList
			.sortWith((recent, old) => recent._1.compareTo(old._1) < 0)
		}
	}

	/**
	 * Generate impact stats from commits (= the number of commits for each contributor)
	 */
	def extractUserActivity(user: String, repo: String) = {
		for {
			commits <- serviceGHRepository.listCommits(user, repo, 100)
			result = commits.map(_.author)
		} yield {
			mergeAndCountList(result).toList.sortWith(_._2 > _._2)
		}
	}

	/**
	 * Extract the prefered languages from all repositories of one user
	 */
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