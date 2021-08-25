package dev.myclinic.scala.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.web.HttpClient
import dev.myclinic.scala.web.Params

import java.time.LocalDate
import scala.concurrent.Future

object Api {

  val client = new HttpClient("/api/")

  def listAppoint(from: LocalDate, upto: LocalDate): Future[List[Appoint]]  = {
    client.get[List[Appoint]]("list-appoint", Params("from" -> from, "upto" -> upto))
  }

}