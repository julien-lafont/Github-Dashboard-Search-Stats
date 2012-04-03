package modules.ws
import play.api.libs.concurrent.Akka
import play.api.libs.ws.Response
import play.api.libs.Files
import play.api.Play.current
import play.api.Play

class ServiceWSMock extends ServiceWS {
	override def get(url: String) = Akka.future(new FakeResponse(url))
	
	class FakeResponse(url:String) extends Response(null) {
		override def header(name: String) = Some("")
		override def status = 200
		override lazy val body = Files.readFile(Play.current.getFile("fixtures/"+url.replaceAll("[/]", ">")))
	}
}
