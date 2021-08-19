package dev.myclinic.scala.web

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model._
import dev.myclinic.scala.web.HttpClient
import dev.myclinic.scala.web.Params
import scala.concurrent.Future

object Api {

  val client = new HttpClient("/pre")

  def hello(){
  }

  def listAppoint(from: LocalDate, upto: LocalDate): Future[List[Appoint]]  = {
    client.get[List[Appoint]]("list-appoint", Params("from" -> from, "upto" -> upto))
  }

}