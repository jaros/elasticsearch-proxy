package controllers

import java.net.InetAddress

import play.Play
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future

class Application extends Controller {


  def index = Action.async {
    Future.successful {
      val appToken = Play.application.configuration.getString("play.crypto.secret")
      Ok(Json.obj(
        "status" -> "OK",
        "remote_ip_address" -> InetAddress.getLocalHost.getHostAddress,
        "token" -> appToken))
    }
  }

  def esProxy(action: String) = Action { request =>
    Ok(s"""
         Ok     => $action
         method => ${request.method}
         path   => ${request.path}
         body   => ${request.body.asJson}
      """)
  }
}