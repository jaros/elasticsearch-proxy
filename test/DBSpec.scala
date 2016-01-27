
import models.Message
import org.specs2.mutable.Specification
import play.api.test.WithApplication
import service.MessageService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

class DBSpec extends Specification {

  // Evolutions.applyEvolutions()

  "DB MessageService" should {

    "retrieve all messages from db" in new WithApplication {
      val messageService = new MessageService

      // read all messages
      var messages = Await.result(messageService.all(), 1 seconds)
      messages should not be empty
      println(messages)
    }

    "remove and add another messages" in new WithApplication {
      val messageService = new MessageService

      val testMessages = Set(
        Message(10, "Hello"),
        Message(11, "Wordl"),
        Message(12, "Aloha")
      )

      // read all messages
      var messages = Await.result(messageService.all(), 1 seconds)
      messages should not be empty

      // delete all messages from evolution script
      Await.result(Future.sequence(List(1,2,3).map(messageService.deleteById)), 1 seconds)
//      Await.result(messageService.deleteAll(), 1 seconds)
      messages = Await.result(messageService.all(), 1 seconds)
      messages should beEmpty


      // write messages to DB
      Await.result(Future.sequence(testMessages.map(messageService.create)), 1 seconds)

      // read all messages
      messages = Await.result(messageService.all(), 1 seconds)
      messages.toSet must equalTo(testMessages)
    }
  }

}
