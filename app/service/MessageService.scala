package service

import javax.inject.Inject

import models.Message
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.Future

class MessageService @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  private val messages = TableQuery[MessageTable]

  def all(): Future[Seq[Message]] = db.run(messages.result)

  def create(message: Message) = dbConfig.db.run(messages returning messages += message)

  def findById(id: Int) = dbConfig.db.run(findQuery(id).result.head)

  def deleteById(id: Int) = dbConfig.db.run(findQuery(id).delete)

  def updateById(id: Int, message: Message) = dbConfig.db.run(findQuery(id).update(message))

  private def findQuery(id: Int) = messages.filter(_.id === id)


  private class MessageTable(tag: Tag) extends Table[Message](tag, "messages") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def message = column[String]("message")

    override def * = (id.?, message) <>((Message.apply _).tupled, Message.unapply _)
  }

}
