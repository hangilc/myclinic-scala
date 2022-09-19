package dev.myclinic.scala.web.appbase.patientdialog

import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.KoukikoureiInputs
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import java.time.LocalDate

object Common:
  def createKoukikoureiInputs(modelOpt: Option[Koukikourei]): Future[KoukikoureiInputs] =
    for
      bangou <- Api.defaultKoukikoureiHokenshaBangou()
    yield 
      new KoukikoureiInputs(modelOpt):
        override def defaultKoukikoureiHokenshaBangou: String = bangou.toString

  def patientBlock(patient: Patient): HTMLElement =
    div(
      innerText := s"(${patient.patientId}) ${patient.lastName} ${patient.firstName}",
      cls := "patient-block"
    )

  def listCurrentHoken(patientId: Int): Future[List[Hoken]] =
    for
      result <- Api.getPatientHoken(patientId, LocalDate.now())
      (_, _, shahokokuho, koukikourei, roujin, kouhi) = result
    yield List.empty[Hoken] ++ shahokokuho ++ koukikourei ++ roujin ++ kouhi

