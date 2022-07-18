package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.PatientInputs
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure

class NewPatientDialog:
  val inputs = new PatientInputs(None)
  val errBox = ErrorBox()
  val dlog = new ModalDialog3()
  dlog.content(cls := "practice-new-patient-dialog")
  dlog.title("新規患者")
  dlog.body(
    inputs.formPanel,
    errBox.ele
  )
  dlog.commands(
    button("入力"), onclick := (onEnter _),
    button("キャンセル", onclick := (dlog.close _))
  )

  def initFocus(): Unit =
    inputs.lastNameInput.getElement.focus()

  def open(): Unit =
    dlog.open()

  private def onEnter(): Unit =
    inputs.validateForEnter() match {
      case Left(msg) => errBox.show(msg)
      case Right(patient) => 
        (for
          _ <- Api.enterPatient(patient)
        yield ()).onComplete {
          case Success(_) => dlog.close()
          case Failure(ex) => errBox.show(ex.toString)
        }
    }
