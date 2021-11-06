package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given

trait SearchResult extends ValuePart:
  def makeNameSlot(patient: Patient): HTMLElement =
    div(hoverBackground("#eee"), padding := "2px 4px", cursor := "pointer")(
      s"(${patient.patientId}) ${patient.fullName()}"
    )

  def populateSearchResult(
      patients: List[Patient],
      appointId: Int,
      cb: () => Unit
  ): Unit =
    initWorkarea()
    patients.foreach(patient => {
      val slot = makeNameSlot(patient)
      addToWorkarea(slot)
      slot(onclick := (() => applyPatient(patient, appointId, cb)))
    })
    addToWorkarea(
      div(
        Icons.xCircle(size = "1.2rem", color = "gray")(
          Icons.defaultStyle,
          floatRight,
          onclick := (initWorkarea _)
        )
      )
    )

  def applyPatient(patient: Patient, appointId: Int, cb: () => Unit): Unit =
    val f =
      for
        appoint <- Api.getAppoint(appointId)
        newAppoint = {
          appoint.copy(
            patientName = patient.fullName("　"),
            patientId = patient.patientId
          )
        }
        patientOption <- Api.findPatient(patient.patientId)
      yield {
        AppointValidator
          .validateForUpdate(newAppoint, patientOption)
          .toEither() match {
          case Right(appoint) => {
            (for _ <- Api.updateAppoint(appoint)
            yield cb()).catchErr
          }
          case Left(msg) => showError(msg)
        }
      }
    f.catchErr
