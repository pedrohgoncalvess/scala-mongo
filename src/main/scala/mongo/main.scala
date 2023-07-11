package mongo

import scala.concurrent.Await

object executer extends App {

  val mongoDB = new MongoDB()
  val document = Person("Pedro", "Carlos", 21)

  val insertResult = mongoDB.insertPerson(document)
  Await.result(insertResult, scala.concurrent.duration.Duration.Inf)

  val queryResult = mongoDB.queryPersons("Pedro")
  Await.result(queryResult, scala.concurrent.duration.Duration.Inf)

  mongoDB.closeConnection

  print("Finalized tasks.")
}