package controllers

import play.api.libs.json._
import play.api.mvc._

class Application extends Controller {



  def index = Action { request =>
    val appToken = play.Play.application.configuration.getString("play.crypto.secret")

    Ok(Json.obj(
      "status" -> "OK",
      "remote_ip_address" -> request.remoteAddress,
      "token" -> appToken))
  }

}