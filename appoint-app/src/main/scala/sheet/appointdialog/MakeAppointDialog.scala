package dev.myclinic.scala.web.appoint.sheet.appointdialog

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.{Icons, Colors, LocalModal, ErrorBox, Modal}
import dev.myclinic.scala.model.{AppointTime, Appoint, Patient}
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Form
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.validator.AppointValidator
import dev.myclinic.scala.validator.AppointValidator.given
import cats.data.Validated.Valid
import cats.data.Validated.Invalid
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom.raw.MouseEvent
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

class MakeAppointDialog(appointTime: AppointTime):
  val ui = MakeAppointUI(appointTime, () => close())
  val dlog = Modal(
    "診察予約入力",
    ui.body(cls := "appoint-dialog-body"),
    ui.commands
  )
  ui.cancelButton(onclick := (() => dlog.close()))

  def open(): Unit = 
    dlog.open()
    ui.nameInput.focus()

  def close(): Unit = dlog.close()
