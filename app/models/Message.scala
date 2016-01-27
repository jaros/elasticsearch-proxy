package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Message(id: Int, message: String)

object Message {
  implicit val messageReads: Reads[Message] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "message").read[String]
    ) (Message.apply _)


  implicit val messageWrites: Writes[Message] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "message").write[String]
    ) (unlift(Message.unapply _))
}