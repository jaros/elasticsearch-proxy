package service

import javax.inject.Inject

import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import play.api.mvc.Results._
import play.api.mvc.{AnyContent, Request, Result}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ElasticSearchClient(wsClient: WSClient, elasticSearchURL: String) {
  @Inject def this(wsClient: WSClient) = this(wsClient, "http://localhost:9200")


  def indexStatus(index: String): Future[Result] = {
    val name = if (index == "all") "" else index
    wsClient
      .url(s"$elasticSearchURL/$name/_stats/docs,store")
      .get()
      .map { response =>
        val content = response.json \ "_all" \ "total"
        Ok(Json.obj(
          "docs_count" -> (content \ "docs" \ "count").get,
          "store_size_in_bytes" -> (content \ "store" \ "size_in_bytes").get))
      }
  }

  def esProxy(urlAction: String)(implicit request: Request[AnyContent]): Future[Result] = {
    val requestBody = request.body.asText
    println(
      s"""
         Ok     => $urlAction
         method => ${request.method}
         path   => ${request.path}
         body   => $requestBody
         header => ${request.headers}
      """)

    wsClient.url(s"$elasticSearchURL/$urlAction")
      .withMethod(request.method)
      .withBody(requestBody.getOrElse("")).execute().map { response =>
       println(response)
      new Status(response.status)(response.json)
    }
  }

}
