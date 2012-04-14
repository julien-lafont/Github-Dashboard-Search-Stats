package modules.ws
import play.api.libs.ws.WS
import play.api.Logger

class ServiceWSPlay extends ServiceWS {
	
	/**
	 * Simply call the WS and return a Promise result
	 */
	override def get(url: String) = {
		Logger.info("URL > "+url);
		WS.url(url.replaceAll(" ", "%20")).get()
	}
}