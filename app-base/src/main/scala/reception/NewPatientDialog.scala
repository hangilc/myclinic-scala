package dev.myclinic.scala.web.appbase.reception

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.PatientInputs
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.model.Patient

class NewPatientDialog(onEnter: Patient => Unit):
  val inputs = new PatientInputs(None)
  val errBox = ErrorBox()
  val dlog = new ModalDialog3()
  dlog.content(cls := "reception-new-patient-dialog")
  dlog.title("新規患者")
  dlog.body(
    inputs.formPanel,
    errBox.ele
  )
  dlog.commands(
    button("入力"), onclick := (doEnter _),
    button("キャンセル", onclick := (dlog.close _))
  )

  def initFocus(): Unit =
    inputs.lastNameInput.getElement.focus()

  def open(): Unit =
    dlog.open()

  private def doEnter(): Unit =
    inputs.validateForEnter() match {
      case Left(msg) => errBox.show(msg)
      case Right(patient) => 
        (for
          entered <- Api.enterPatient(patient)
        yield entered).onComplete {
          case Success(entered) =>
            println(("new-patient", entered))
            dlog.close() 
            onEnter(entered)
          case Failure(ex) => errBox.show(ex.toString)
        }
    }
