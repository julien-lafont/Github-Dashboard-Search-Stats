package modules

import sindi._
import modules.ws._
import modules.jsonWs._
import modules.github._
import modules.yahoo._
import modules.stats.ModuleExtractData

object AppTestContext extends Context {
	override lazy val modules =
		new ModuleGithubAuthor(this) ::
		new ModuleGithubRepository(this) ::
		new ModuleYahooWS(this) ::
		new ModuleWS(this) ::
		new ModuleJsonWS(this) ::
		new ModuleExtractData(this) ::
		Nil

	// Dans l'environnement de test, on mock les appels aux WebServices
	override val bindings: Bindings = bind[ServiceWS] to new ServiceWSMock()

	// Services publics
	val serviceJsonWs = from[ModuleJsonWS].service
	val serviceGithubAuthor = from[ModuleGithubAuthor].service
	val serviceGithubRepository = from[ModuleGithubRepository].service
	val serviceYahooWs = from[ModuleYahooWS].service
	val serviceStats = from[ModuleExtractData].service

}