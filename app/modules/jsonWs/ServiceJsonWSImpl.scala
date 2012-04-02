package modules.jsonWs

import modules.ws._
import utils.Query
import play.api.libs.concurrent.Promise
import play.api.libs.json.JsValue
import play.api.cache.Cache
import play.api.Play.current

class ServiceJsonWSImpl(override val serviceWs: ServiceWS) extends ServiceJsonWS {
	
	override def fetch(query: Query)  = {
		fetchIfQueryValid(query)(getResult(_))
	}
	
	override def fetchWithCache(query: Query, expiration:Int=3600) = {
		fetchIfQueryValid(query)( url =>
			Cache.getOrElse(url, expiration)(getResult(url))
		)
	}
	
	private def fetchIfQueryValid(query:Query)(actionIfQueryValid: (String) => Promise[JsValue]) = {
		query.toUrl match {
			case Some(url: String) if query.isComplete =>
				actionIfQueryValid(url)
			case _ => throw new Exception("fuck")
		}
	}
	
	private def getResult(url: String) = {
		serviceWs.get(url).map(_.json)
	}
}