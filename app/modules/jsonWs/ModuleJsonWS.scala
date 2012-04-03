package modules.jsonWs

import sindi._
import modules.ws._
import utils.Query
import play.api.libs.concurrent.Promise
import play.api.libs.json.JsValue

final class ModuleJsonWS(override val ctx: Context) extends Module {
	override lazy val modules = new ModuleWS(this) :: Nil
	override val bindings: Bindings = bind[ServiceJsonWS] to new ServiceJsonWSImpl(from[ModuleWS].service)

	val service = inject[ServiceJsonWS]
}

trait ServiceJsonWS {
	val serviceWs: ServiceWS
	def fetch(query: Query): Promise[JsValue]
	def fetchWithCache(query: Query, expiration: Int = 3600): Promise[JsValue]
}