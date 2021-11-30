package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber, Selection}
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.web.appbase.DateInput
import dev.myclinic.scala.model.{Sex, Patient}
import java.time.LocalDateTime
import java.time.LocalDate

class ManagePatientBlock(patient: Patient, onClose: ManagePatientBlock => Unit):
  val eDispWrapper = div(PatientDisp(patient).ele)
  val block = Block(
    s"${patient.fullName()} (${patient.patientId})",
    div(cls := "manage-patient-block-content")(
      div(cls := "left")(eDispWrapper),
      div(cls := "right")
    ),
    div(
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  block.ele(cls := "manage-patient-block")
  val ele = block.ele
  def init(): Unit =
    val date = LocalDate.now()
    for
      shahoOpt <- Api.findAvailableShahokokuho(patient.patientId, date)
      roujinOpt <- Api.findAvailableRoujin(patient.patientId, date)
      koukikoureiOpt <- Api.findAvailableKoukikourei(patient.patientId, date)
      kouhiList <- Api.listAvailableKouhi(patient.patientId, date)
    yield {
      println(("shahoOpt", shahoOpt, roujinOpt, koukikoureiOpt, kouhiList))
    }
