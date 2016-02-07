import akka.util.Timeout
import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.Json
import play.api.mvc.{Action, Results}
import play.api.routing.sird._
import play.api.test._
import play.core.server.Server
import service.ElasticSearchClient

import scala.concurrent.duration.DurationInt

@RunWith(classOf[JUnitRunner])
class WSClientSpec extends Specification {

  implicit val timeout: Timeout = 10 seconds

  "ElasticSearch client" should {
    "send request to ES server with body content" in {

      val fakeRequest = FakeRequest("PUT", "/privet_index/person/2").withTextBody(
        """{
             "user" : "Bob3",
             "post_date" : "2009-11-15T14:12:12",
              "message" : "ttest3"
              }""")

      Server.withRouter() {
        // ES server emulation
        case PUT(p"/privet_index/person/2") => Action {
          Results.Ok(Json.obj("full_name" -> "octocat/Hello-World"))
        }
      } { implicit port =>
        WsTestClient.withClient { client =>

          val result = new ElasticSearchClient(client, "").esProxy(fakeRequest, "privet_index/person/2")
          println("result.body -- " + Helpers.contentAsString(result))
          Helpers.contentAsJson(result) must beEqualTo(Json.obj("full_name" -> "octocat/Hello-World"))
        }
      }
    }
  }


}


