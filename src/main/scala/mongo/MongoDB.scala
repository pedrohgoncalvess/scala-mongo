package mongo

import reactivemongo.api.bson.BSONDocument.pretty
import reactivemongo.api.bson.collection.BSONCollection

import scala.concurrent.{ExecutionContext, Future}
import reactivemongo.api.{AsyncDriver, MongoConnection}
import reactivemongo.api.bson.{BSONDocument, BSONDocumentWriter, Macros}


class MongoDB(connectionUri:String="mongodb://root:admin@localhost:9090/?authSource=admin",
                      databaseName:String="teste",
                      collectionName:String = "teste"
                      ) {
  import ExecutionContext.Implicits.global

  private val driver = new AsyncDriver
  private val connection: Future[MongoConnection] = connectionDriver
  private val collection: Future[BSONCollection] = collectionDb
  implicit val personWriter: BSONDocumentWriter[Person] = Macros.writer[Person]

  def connectionDriver: Future[MongoConnection] = {
    val connection: Future[MongoConnection] = for {
      parsedUri <- MongoConnection.fromString(connectionUri)
      connection <- this.driver.connect(parsedUri)
    } yield connection
    println("Connecting to MongoDB")
    connection
  }

  def closeConnection: Unit = {
    this.driver.close()
  }

  def collectionDb: Future[BSONCollection] = {
    this.connection.flatMap { conn =>
      conn.database(this.databaseName).map(_.collection(collectionName))
    }
  }

  def insertPerson(document: Person): Future[Unit] = {
    this.collection.flatMap { collection =>
      collection.insert.one(document).map { _ =>
        println("Document inserted successfully")
      }
    }
  }

  def queryPersons(firstName: String): Future[Unit] = {
    this.collection.flatMap { collection =>
      val query = BSONDocument("first_name" -> firstName)
      collection.find(query).cursor[BSONDocument]().collect[List]().map { results =>
        results.foreach(result => println(pretty(result)))
      }
    }
  }
}