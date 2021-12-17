package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, Selection}
import scala.language.implicitConversions
import scala.util.{Success, Failure}
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.{EventSubscriber}
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.model.{Sex, Patient}
import java.time.LocalDateTime

class SearchPatientBlock(
    patients: List[Patient],
    onClose: SearchPatientBlock => Unit,
    onManagePatient: Patient => Unit
):
  val errorBox = ErrorBox()
  var disp: Option[PatientDisp] = None
  val eResult =
    Selection[Patient](
      patients.map(patient => itemRep(patient) -> patient),
      onSelect(_)
    )
  val eDisp: HTMLElement = div()
  val block = Block(
    "患者検索結果",
    div(cls := "content")(
      div(cls := "left-pane")(
        eResult.ele
      ),
      div(cls := "right-pane")(
        errorBox.ele,
        eDisp
      )
    ),
    div(
      button("診察受付", onclick := (onRegisterForExam _)),
      button("患者管理", onclick := (onManage _)),
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  block.ele(cls := "search-patient-block")
  if patients.size == 1 then onSelect(patients(0))

  val ele = block.ele
  def remove(): Unit = ele.remove()
  private def itemRep(patient: Patient): String =
    String.format(
      "[%04d] %s",
      patient.patientId,
      patient.fullName()
    )

  private def onSelect(patient: Patient): Unit =
    val newDisp = PatientDisp(patient)
    eDisp.innerHTML = ""
    eDisp(newDisp.ele)
    disp = Some(newDisp)

  private def withCurrentPatient(f: Patient => Unit): Unit =
    disp.foreach(d => f(d.patient))

  private def onRegisterForExam(): Unit =
    withCurrentPatient(patient => {
      Api.startVisit(patient.patientId, LocalDateTime.now()).onComplete {
        case Success(_)  => onClose(this)
        case Failure(ex) => errorBox.show(ex.getMessage)
      }
    })

  private def onManage(): Unit =
    withCurrentPatient(patient => {
      onClose(this)
      onManagePatient(patient)
    })
