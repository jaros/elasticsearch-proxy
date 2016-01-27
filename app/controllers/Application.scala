package controllers

import javax.inject.Inject

import models.Message
import play.api.libs.json._
import play.api.mvc._
import service.MessageService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(messageService: MessageService) extends Controller {

  def index = Action.async {
//    Ok("Your new application is ready.")
    messageService.all().map(msgs => Ok(Json.obj("status" -> "Ok", "messages" -> Json.toJson(msgs))))
  }

  def create = Action.async(BodyParsers.parse.json) { request =>
    val message = request.body.validate[Message]
    message.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Parsing message failed",
        "error" -> JsError.toJson(errors)))),
      message =>
        messageService.create(message).map(m =>
          Ok(Json.obj("status" -> "Success", "message" -> Json.toJson(m)))
        ))
  }

  def show(id: Int) = Action.async {
    messageService.findById(id).map(msg => Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(msg))))
  }

  def update(id: Int) = Action.async(BodyParsers.parse.json) { request =>
    val message = request.body.validate[Message]
    message.fold(
      errors => Future(BadRequest(Json.obj(
        "status" -> "Message update failed",
        "error" -> JsError.toJson(errors)))),
      message => {
        val res: Future[Int] = messageService.updateById(id, message)
        Future(Ok(Json.obj("status" -> "Ok", "message" -> Json.toJson(message))))
      }
    )
  }

  def delete(id: Int) = Action.async {
    messageService.deleteById(id).map(m => Ok(Json.obj("status" -> "Ok")))
  }
}