package modules.ws
import play.api.libs.ws.WS

class ServiceWSPlay extends ServiceWS {
	override def get(url: String) = {
		println("URL > "+url);
		WS.url(url.replaceAll(" ", "%20")).get()
	}
}