package modules

import sindi._
import modules.ws._
import modules.jsonWs._
import modules.github._
import modules.yahoo._

object AppTestContext extends Context {
  override lazy val modules = 
  	new ModuleGithubAuthor(this) :: 
  	new ModuleGithubRepository(this) ::
  	new ModuleYahooWS(this) :: 
  	new ModuleWS(this) :: 
  	new ModuleJsonWS(this) :: 
  	Nil

  // Dans l'environnement de test, on mock les appels aux WebServices
  override val bindings: Bindings = bind[ServiceWS] to new ServiceWSMock()
  
  val serviceJsonWs = from[ModuleJsonWS].service
  val serviceGithubAuthor = from[ModuleGithubAuthor].service
  val serviceGithubRepository = from[ModuleGithubRepository].service
  val serviceYahooWs = from[ModuleYahooWS].service
   
}