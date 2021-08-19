package dev.myclinic.scala.web

import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model._
import dev.myclinic.scala.web.HttpClient

object Api {

  def hello(){
    HttpClient.hello()
  }
  // def listAppoint(from: LocalDate, upto: LocalDate): Future[List[Appoint]] = {

  // }

}