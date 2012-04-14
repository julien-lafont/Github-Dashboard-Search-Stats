package modules.yahoo

import sindi._
import modules.jsonWs._
import play.api.libs.concurrent.Promise
import play.api.libs.json.JsValue
import utils.Query

final class ModuleYahooWS(override val ctx: Context) extends Module {
	override lazy val modules = new ModuleJsonWS(this) :: Nil
  override val bindings: Bindings = bind[ServiceYahooWS] to new ServiceYahooWSImpl(from[ModuleJsonWS].service)
	
  val service = inject[ServiceYahooWS]
}

trait ServiceYahooWS {
	val serviceJsonWs: ServiceJsonWS 
	def findLocation(location: String) : Promise[JsValue]
}

class ServiceYahooWSImpl(override val serviceJsonWs: ServiceJsonWS) extends ServiceYahooWS  {
	
	/**
	 * Retreive geopositioning informations from a location (city, address, country)
	 */
	override def findLocation(location: String) = {
		serviceJsonWs.fetchWithCache(Query("yahoo.query.geoloc").set("query" -> location))
	}
}