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
  val eResult = Selection[Patient](
    patients.map(patient =>
      String.format(
        "[%04d] %s",
        patient.patientId,
        patient.fullName()
      ) -> patient
    )
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
      button("閉じる", onclick := (() => onClose(this)))
    )
  )
  block.ele(cls := "search-patient-block")
  val ele = block.ele
  def remove(): Unit = ele.remove()
