package dev.myclinic.scala.webclient

import java.time.{LocalDate, LocalTime}
import scala.concurrent.Future
import dev.myclinic.scala.model._
import io.circe._
import io.circe.syntax._
import dev.myclinic.scala.model.jsoncodec.Implicits.{given}
import dev.myclinic.scala.webclient.ParamsImplicits.{given}
import scala.language.implicitConversions

object PatientApi extends ApiBase:
  def baseUrl: String = "/api/"

  trait Api:
    def getPatient(patientId: Int): Future[Patient] = 
      get("get-patient", Params("patient-id" -> patientId))

    def findPatient(patientId: Int): Future[Option[Patient]] = 
      if patientId <= 0 then Future.successful(None)
      else get("find-patient", Params("patient-id" -> patientId))

    def searchPatient(text: String): Future[(Int, List[Patient])] = 
      get("search-patient", Params("text" -> text))

    def searchPatientSmart(text: String): Future[List[Patient]] =
      val emptyPattern = raw"\s*".r
      val digitsPattern = raw"\d+".r
      text match {
        case null | emptyPattern() => Future.successful(List.empty)
        case digitsPattern(digits) => findPatient(digits.toInt).map(_.toList)
        case t => searchPatient(t).map(_._2)
      }

    def batchGetPatient(patientIds: List[Int]): Future[Map[Int, Patient]] =
      post("batch-get-patient", Params(), patientIds)

    def enterPatient(patient: Patient): Future[Patient] =
      post("enter-patient", Params(), patient)

    def updatePatient(patient: Patient): Future[Boolean] =
      post("update-patient", Params(), patient)