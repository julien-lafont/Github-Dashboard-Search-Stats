package modules

import sindi._
import modules.ws._
import modules.jsonWs._
import modules.github._
import modules.yahoo._
import modules.bi.ModuleExtractData
import modules.bi.ModuleExtractData

object AppContext extends Context {
	override lazy val modules =
		new ModuleGithubAuthor(this) ::
		new ModuleGithubRepository(this) ::
		new ModuleYahooWS(this) ::
		new ModuleWS(this) ::
		new ModuleJsonWS(this) ::
		new ModuleExtractData(this) ::
		Nil

	// Services accessible in controllers
	val serviceGithubAuthor = from[ModuleGithubAuthor].service
	val serviceGithubRepository = from[ModuleGithubRepository].service
	val serviceYahooWs = from[ModuleYahooWS].service
	val serviceStats = from[ModuleExtractData].service

}