package models

import play.api.libs.functional.syntax._
import play.api.libs.json._

case class Message(id: Int, content: String)

object Message {
  implicit val messageReads: Reads[Message] = (
    (JsPath \ "id").read[Int] and
      (JsPath \ "content").read[String]
    ) (Message.apply _)


  implicit val messageWrites: Writes[Message] = (
    (JsPath \ "id").write[Int] and
      (JsPath \ "content").write[String]
    ) (unlift(Message.unapply _))
}