package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber}
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari

class ManagePatientBlock(var patient: Patient, onClose: ManagePatientBlock => Unit):
  import ManagePatientBlock.Disp
  val eLeftPane = div
  val eRightPane = div()
  val eSubblocks = div()
  val hokenList = HokenList(patient.patientId, eSubblocks)
  val block = Block(
    s"${patient.fullName()} (${patient.patientId})",
    div(cls := "manage-patient-block-content")(
      eLeftPane(cls := "left"),
      eRightPane(cls := "right")(
        hokenList.ele
      )
    ),
    div(
      button("新規社保国保", onclick := (onNewShahokokuho _)),
      button("新規後期高齢", onclick := (onNewKoukikourei _)),
      button("新規公費", onclick := (onNewKouhi _)),
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  updateLeftPane()
  block.ele(cls := "manage-patient-block")
  block.ele(eSubblocks(cls := "subblocks"))
  val ele = block.ele

  def updateLeftPane(): Unit = eLeftPane.setChild(Disp(patient, onEditPatient).ele)

  def init(): Unit =
    hokenList.init()

  def onEditPatient(): Unit =
    val form = PatientEdit(patient)
    form.onCancel = () => 
      println("cancel")
      updateLeftPane()
    form.onDone = () =>
      for
        updated <- Api.getPatient(patient.patientId)
      yield
        patient = updated
        updateLeftPane()
    eLeftPane.setChild(form.ele)

  private def onNewShahokokuho(): Unit =
    val b = new NewShahokokuhoSubblock(patient.patientId)
    eSubblocks.prepend(b.block.ele)

  private def onNewKoukikourei(): Unit =
    val b = new NewKoukikoureiSubblock(patient.patientId)
    eSubblocks.prepend(b.block.ele)

  private def onNewKouhi(): Unit =
    val b = new NewKouhiSubblock(patient.patientId)
    eSubblocks.prepend(b.block.ele)

object ManagePatientBlock:
  class Disp(ui: DispUI, patient: Patient):
    ui.dispWrapper(PatientDisp(patient).ele)
    def ele = ui.ele

  object Disp:
    def apply(patient: Patient, onEdit: () => Unit): Disp =
      val ui = new DispUI
      ui.editButton(onclick := onEdit)
      new Disp(ui, patient)

  class DispUI:
    val dispWrapper = div
    val editButton = button
    val ele = div(
      dispWrapper,
      div(editButton("編集"))
    )