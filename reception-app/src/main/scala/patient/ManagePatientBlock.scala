package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.*
import java.time.LocalDateTime
import java.time.LocalDate
import dev.myclinic.scala.util.{HokenRep, RcptUtil}
import dev.myclinic.scala.apputil.FutanWari
import dev.myclinic.scala.web.appbase.{EventFetcher}
import dev.myclinic.scala.web.appbase.SyncedDataSource

class ManagePatientBlock(ds: => SyncedDataSource[Patient])(using EventFetcher):
  import ManagePatientBlock.*
  def gen: Int = ds.gen
  def patient: Patient = ds.data
  val eLeftPane = div
  val eRightPane = div()
  val eSubblocks = div()
  val hokenList = HokenList(patient.patientId)
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
      button("閉じる", onclick := (onClose _))
    )
  )
  updateLeftPane()
  block.ele(cls := "manage-patient-block")
  block.ele(eSubblocks(cls := "subblocks"))
  CustomEvents.addHokenSubblock[Shahokokuho].handle(
    ele,
    onAddShahokokuhoSubblock.tupled
  )
  CustomEvents.addHokenSubblock[Koukikourei].handle(
    ele,
    onAddKoukikoureiSubblock.tupled
  )
  CustomEvents.addHokenSubblock[Kouhi].handle(
    ele,
    onAddKouhiSubblock.tupled
  )
  CustomEvents.addHokenSubblock[Roujin].handle(
    ele,
    onAddRoujinSubblock.tupled
  )
  ds.startSync(ele)

  def ele = block.ele

  private def onAddShahokokuhoSubblock(
      gen: Int,
      shahokokuho: Shahokokuho
  ): Unit =
    val sub = ShahokokuhoSubblock(() => SyncedDataSource(gen, shahokokuho))
    eSubblocks(sub.ele)

  private def onAddKoukikoureiSubblock(
      gen: Int,
      koukikourei: Koukikourei
  ): Unit =
    val sub = KoukikoureiSubblock(() => SyncedDataSource(gen, koukikourei))
    eSubblocks(sub.ele)

  private def onAddKouhiSubblock(
      gen: Int,
      kouhi: Kouhi
  ): Unit =
    val sub = KouhiSubblock(gen, kouhi)
    eSubblocks(sub.ele)

  private def onAddRoujinSubblock(
      gen: Int,
      roujin: Roujin
  ): Unit =
    val sub = RoujinSubblock(gen, roujin)
    eSubblocks(sub.ele)

  def updateLeftPane(): Unit =
    eLeftPane(clear, PatientDispPane(patient, onEditPatient).ele)

  def onClose(): Unit = block.ele.remove()

  def onEditPatient(): Unit =
    val form = PatientEdit(patient)
    form.onCancel = () => updateLeftPane()
    form.onDone = () => updateLeftPane()
    eLeftPane(clear, form.ele)

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
  class PatientDispPane(ui: PatientDispPaneUI, patient: Patient):
    ui.dispWrapper(PatientDisp(patient).ele)
    def ele = ui.ele

  object PatientDispPane:
    def apply(patient: Patient, onEdit: () => Unit): PatientDispPane =
      val ui = new PatientDispPaneUI
      ui.editButton(onclick := onEdit)
      new PatientDispPane(ui, patient)

  class PatientDispPaneUI:
    val dispWrapper = div
    val editButton = button
    val ele = div(
      dispWrapper,
      div(editButton("編集"))
    )
