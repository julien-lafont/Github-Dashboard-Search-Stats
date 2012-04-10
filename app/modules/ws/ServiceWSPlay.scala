package modules.ws
import play.api.libs.ws.WS

class ServiceWSPlay extends ServiceWS {
	override def get(url: String) = {
		WS.url(url.replaceAll(" ", "+")).get()
	}
}