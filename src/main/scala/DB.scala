package dev.myclinic.scala.db

import doobie._
import doobie.implicits._
import dev.myclinic.scala.model._
import dev.myclinic.scala.db.DoobieMapping._

object DB {
  
  def getPatient(patientId: Int) = {
    sql"select * from patient where patient_id = $patientId".query[Patient].unique
  }

}
