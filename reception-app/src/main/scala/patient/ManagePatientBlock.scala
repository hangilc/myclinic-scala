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
import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari

class ManagePatientBlock(patient: Patient, onClose: ManagePatientBlock => Unit):
  val eDispWrapper = div(PatientDisp(patient).ele)
  val eRightPane = div()
  val eSubblocks = div()
  val hokenList = HokenList(patient.patientId, eSubblocks)
  val block = Block(
    s"${patient.fullName()} (${patient.patientId})",
    div(cls := "manage-patient-block-content")(
      div(cls := "left")(eDispWrapper),
      eRightPane(cls := "right")(
        hokenList.ele
      )
    ),
    div(
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  block.ele(cls := "manage-patient-block")
  block.ele(eSubblocks(cls := "subblocks"))
  val ele = block.ele

  def init(): Unit =
    hokenList.init()
