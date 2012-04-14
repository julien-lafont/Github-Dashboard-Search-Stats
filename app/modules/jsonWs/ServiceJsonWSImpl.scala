package modules.jsonWs

import modules.ws._
import utils.Query
import play.api.libs.concurrent.Promise
import play.api.cache.Cache
import play.api.Play.current
import play.api.PlayException
import play.api.libs.json._

class ServiceJsonWSImpl(override val serviceWs: ServiceWS) extends ServiceJsonWS {

	/**
	 * Fetch data in json format if the query is valid, or throw a PlayException
	 */
	override def fetch(query: Query) = {
		fetchIfQueryValid(query)(getResult(_))
	}

	/**
	 * Return data from cache if this query was already called, of fetch them from the WS
	 */
	override def fetchWithCache(query: Query, expiration: Int = 3600) = {
		fetchIfQueryValid(query)(url =>
			Cache.getOrElse(url, expiration)(getResult(url)))
	}
	
	/**
	 * Return the unmarshalled object if there is at least one result, otherwise return None
	 * Data are retrieve from cache if this query was already called, of from the WS
	 */
	override def fetchModel[T](query: Query, expiration: Int = 3600)(implicit fjs: Reads[T]): Promise[Option[T]] = {
		getModelOrNone[T](fetchWithCache(query, expiration))
	}
	
	
	/**
	 * If the json seems valid, try to unmarshall the object, otherwise return None
	 */
	private def getModelOrNone[T](json: Promise[JsValue])(implicit fjs: Reads[T]) : Promise[Option[T]] = {
		json.map(res => res.\("message").asOpt[String] match {
			case Some("Not Found") => None
			case _ => Some(res.as[T])
		})
	}

	/**
	 * Call WS if the query is valid (url exist and is complete)
	 */
	private def fetchIfQueryValid(query: Query)(actionIfQueryValid: (String) => Promise[JsValue]) = {
		query.toUrl match {
			case Some(url: String) if query.isComplete =>
				actionIfQueryValid(url)
			case Some(url: String) if !query.isComplete =>
				throw new PlayException("Bad Request", "Url is malformed "+url)
			case _ =>
				throw new PlayException("Bad Request", "Request is invalid")
		}
	}

	/**
	 * Shortcut to call WS and return json body
	 */
	private def getResult(url: String) = {
		serviceWs.get(url).map(_.json)
	}
	
}