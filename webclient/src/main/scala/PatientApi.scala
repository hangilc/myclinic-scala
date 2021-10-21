package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.modeljson.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object PatientApi extends ApiBase:
  def baseUrl: String = "/api/"

  object Api:
    def getPatient(patientId: Int): Future[Patient] = 
      ???

    def searchPatient(text: String): Future[List[Patient]] = 
      ???