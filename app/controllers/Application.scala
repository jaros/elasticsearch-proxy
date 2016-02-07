package controllers

import java.net.InetAddress
import javax.inject.Inject

import play.Play
import play.api.libs.json._
import play.api.mvc._
import service.ElasticSearchClient

import scala.concurrent.Future

class Application @Inject()(esClient: ElasticSearchClient) extends Controller {

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
    esClient.indexStatus(indexName)
  }

  def esProxy(action: String) = Action.async { implicit request =>
    esClient.esProxy(action)
  }
}