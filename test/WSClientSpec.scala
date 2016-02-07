import org.junit.runner.RunWith
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.JsValue
import play.api.libs.ws.{WSRequest, WS}
import play.api.test.Helpers._
import play.api.test.{FakeHeaders, FakeRequest, WithApplication}

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

@RunWith(classOf[JUnitRunner])
class WSClientSpec extends Specification {

  import scala.concurrent.ExecutionContext.Implicits.global


  "Application" should {
    "send request to ES server with body content" in new WithApplication {


      val reqBody: String =
        """{
             "user" : "Bob3",
             "post_date" : "2009-11-15T14:12:12",
              "message" : "ttest3"
              }"""
//      val reqCompose: WSRequest = WS.url("http://localhost:9200/piter_index/pii/4").withMethod("PUT").withBody(reqBody)

//      println(reqCompose)
//      val response: JsValue = Await.result(reqCompose
//        .execute().map(request => request.json), 5 seconds)

//      val response = route(FakeRequest(PUT, "/es/piter_index/pii/4", FakeHeaders(), reqBody)).get

//      println(contentAsString(response))
    }
  }


}


