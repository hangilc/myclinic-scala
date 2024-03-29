package dev.myclinic.scala.web.appoint.sheet.appointdialog

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.web.appbase.AppointValidator
import dev.myclinic.scala.web.appbase.AppointValidator.given

trait SearchResult extends ValuePart:
  def makeNameSlot(patient: Patient): HTMLElement =
    div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
      s"(${patient.patientId}) ${patient.fullName()}"
    )

  def populateSearchResult(
      patients: List[Patient],
      cb: Patient => Unit
  ): Unit =
    initWorkarea()
    patients.foreach(patient => {
      val slot = makeNameSlot(patient)
      addToWorkarea(slot)
      slot(onclick := (() => cb(patient)))
    })
    addToWorkarea(
      div(
        Icons.xCircle(
          Icons.defaultStyle,
          floatRight,
          onclick := (initWorkarea _)
        )
      )
    )

