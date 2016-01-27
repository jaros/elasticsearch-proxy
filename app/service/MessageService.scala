package service

import models.Message
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile

import scala.concurrent.Future

class MessageService extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  private val messages = TableQuery[MessageTable]

  def all(): Future[Seq[Message]] = db.run(messages.result)

  def create(message: Message) = dbConfig.db.run(messages += message)

  def findById(id: Int) = dbConfig.db.run(findQuery(id).result.head)

  def deleteById(id: Int) = dbConfig.db.run(findQuery(id).delete)

  def deleteAll() = dbConfig.db.run(messages.delete)

  def updateById(id: Int, message: Message) = dbConfig.db.run(findQuery(id).update(message))

  private def findQuery(id: Int) = messages.filter(_.id === id)


  private class MessageTable(tag: Tag) extends Table[Message](tag, "MESSAGES") {
    def id = column[Int]("id", O.PrimaryKey)

    def message = column[String]("message")

    override def * = (id, message) <>((Message.apply _).tupled, Message.unapply _)
  }

}
