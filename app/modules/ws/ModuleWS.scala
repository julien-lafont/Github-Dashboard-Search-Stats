package modules.ws

import sindi._
import play.api.libs.concurrent.Promise
import play.api.libs.ws._

final class ModuleWS(override val ctx: Context) extends Module {
	override val bindings: Bindings = bind[ServiceWS] to new ServiceWSPlay
	val service = inject[ServiceWS]
}

trait ServiceWS {
	def get(url: String): Promise[Response]
}