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

class SearchPatientBlock(
    patients: List[Patient],
    onClose: SearchPatientBlock => Unit
):
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
      eDisp(cls := "right-pane")
    ),
    div(
      button("診察受付"),
      button("患者管理"),
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  block.ele(cls := "search-patient-block")
  val ele = block.ele
  def remove(): Unit = ele.remove()
  private def itemRep(patient: Patient): String =
    String.format(
      "[%04d] %s",
      patient.patientId,
      patient.fullName()
    )
  private def onSelect(patient: Patient): Unit =
    val disp = PatientDisp(patient)
    eDisp.innerHTML = ""
    eDisp(
      div(cls := "patient-disp-wrapper")(
        disp.ele(cls := "disp"),
        div(cls := "commands")(
          button("編集")
        )
      )
    )
