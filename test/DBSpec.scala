
import models.Message
import org.specs2.mutable.Specification
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.WithApplication
import service.MessageService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future}

object AppHelper {
  def mockApplication: Application = new GuiceApplicationBuilder().disable(classOf[AppModule]).build
}

class DBSpec extends Specification {

  // Evolutions.applyEvolutions()

  import AppHelper.mockApplication


  "DB MessageService" should {

    "retrieve all messages from db" in new WithApplication(mockApplication) {
      val messageService = new MessageService

      // read all messages
      var messages = Await.result(messageService.all(), 1 seconds)
      messages should not be empty
      println(messages)
    }

    "add Message with autoIncrement" in new WithApplication(mockApplication) {
      val messageService = new MessageService
      val createdMessage: Message = Await.result(messageService.create("test message"), 1 seconds)

      createdMessage.id should beEqualTo(4)

      var messages = Await.result(messageService.all(), 1 seconds)
      messages should not be empty

      println("added message: " + messages)
    }

    "remove and add another messages" in new WithApplication(mockApplication) {
      val messageService = new MessageService

      val testMessages = Set("Hello", "Wordl", "Aloha")

      // read all messages
      var messages = Await.result(messageService.all(), 1 seconds)
      messages should have size (3)

      // delete all messages from evolution script
      Await.result(Future.sequence(List(1, 2, 3).map(messageService.deleteById)), 1 seconds)
      //      Await.result(messageService.deleteAll(), 1 seconds)
      messages = Await.result(messageService.all(), 1 seconds)
      messages should beEmpty


      // write messages to DB
      Await.result(Future.sequence(testMessages.map(messageService.create)), 1 seconds)

      // read all messages
      messages = Await.result(messageService.all(), 1 seconds)
      messages.map(_.content).toSet must equalTo(testMessages.toSet)
    }
  }

}
