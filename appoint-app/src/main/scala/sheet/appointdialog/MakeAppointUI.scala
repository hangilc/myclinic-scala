package dev.myclinic.scala.web.appoint.sheet.appointdialog

import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Form}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{AppointTime}
import dev.myclinic.scala.web.appoint.Misc

trait MakeAppointUI:
  val body: HTMLElement
  val commands: HTMLElement
  val cancelButton: HTMLElement

object MakeAppointUI:
  def apply(appointTime: AppointTime): MakeAppointUI =
    new MakeAppointUI:
      val cancelButton = button("キャンセル")
      val body: HTMLElement =
        div(
          div(Misc.formatAppointTimeSpan(appointTime)),
          Form.rows(
            
          )(cls := "appoint-dialog-form-table")
        )
      val commands: HTMLElement =
        div(cancelButton)
