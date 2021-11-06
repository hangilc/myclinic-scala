package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Form}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{Appoint, AppointTime}
import dev.myclinic.scala.web.appoint
import appoint.Misc
import org.scalajs.dom.raw.HTMLElement

trait UI:
  val body: HTMLElement
  val commands: HTMLElement
  val execCancelButton: HTMLElement
  val closeButton: HTMLElement
  def onAppointChanged(newAppoint: Appoint): Unit
    

object UI:
  def apply(appoint: Appoint, appointTime: AppointTime): UI =
    new UI:
      val execCancelButton = button("予約取消実行")
      val closeButton = button("閉じる")
      val patientNamePart = PatientNamePart(appoint)
      val patientIdPart = PatientIdPart(appoint)
      val memoPart = MemoPart(appoint)
      val body = div(
        div(Misc.formatAppointTimeSpan(appointTime)),
        Form.rows(
          patientNamePart.keyPart -> patientNamePart.valuePart,
          patientIdPart.keyPart -> patientIdPart.valuePart,
          memoPart.keyPart -> memoPart.valuePart
        )(cls := "edit-appoint-form-table")
      )
      val commands = div(execCancelButton, closeButton)
      def onAppointChanged(newAppoint: Appoint): Unit =
        patientNamePart.onAppointChanged(newAppoint)
        patientIdPart.onAppointChanged(newAppoint)
        memoPart.onAppointChanged(newAppoint)
