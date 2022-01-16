package dev.myclinic.scala.web.appoint.sheet.appointdialog.edit

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import dev.myclinic.scala.web.appoint.sheet.appointdialog.ValuePart
import dev.myclinic.scala.web.appoint.sheet.appointdialog.{SearchResult => BaseSearchResult}

trait SearchResult extends ValuePart with BaseSearchResult:
  def populateSearchResult(
      patients: List[Patient],
      appointId: Int,
      cb: () => Unit
  ): Unit =
    super.populateSearchResult(patients, patient => {
      applyUpdate(patient, appointId, cb)
    })

  def applyUpdate(patient: Patient, appointId: Int, cb: () => Unit): Unit =
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
          .asEither match {
          case Right(appoint) => {
            (for _ <- Api.updateAppoint(appoint)
            yield cb()).catchErr
          }
          case Left(msg) => showError(msg)
        }
      }
    f.catchErr
