package controllers

import java.net.InetAddress
import javax.inject.Inject

import play.Play
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(wsClient: WSClient) extends Controller {

  val elasticSearchURL: String = "http://localhost:9200"

  def index = Action.async {
    Future.successful {
      val appToken = Play.application.configuration.getString("play.crypto.secret")
      Ok(Json.obj(
        "status" -> "OK",
        "remote_ip_address" -> InetAddress.getLocalHost.getHostAddress,
        "token" -> appToken))
    }
  }

  def indexInfo(indexName: String) = Action.async {
    val name = if (indexName == "all") "" else indexName
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

  def esProxy(action: String) = Action.async { request =>

    val requestBody = request.body.asText
    println(
      s"""
         Ok     => $action
         method => ${request.method}
         path   => ${request.path}
         body   => $requestBody
         header => ${request.headers}
      """)

    wsClient.url(s"$elasticSearchURL/$action")
      .withMethod(request.method)
      .withBody(requestBody.getOrElse("")).execute().map { response =>
      new Status(response.status)(response.json)
    }
  }
}