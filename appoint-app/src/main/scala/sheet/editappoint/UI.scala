package dev.myclinic.scala.web.appoint.sheet.editappoint

import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.{Form}
import scala.language.implicitConversions
import dev.myclinic.scala.model.{Appoint, AppointTime}
import dev.myclinic.scala.web.appoint
import appoint.Misc

class UI(
    appointTime: AppointTime,
    appointId: Int,
    var patientName: String,
    var patientId: Int
):
  val execCancelButton = button("予約取消実行")
  val closeButton = button("閉じる")
  private val patientIdPart = PatientIdPart(patientId, appointId, patientName)
  val body = div(
    div(timesRep),
    div(patientName),
    Form.rows(
      patientIdPart.keyPart -> patientIdPart.valuePart
    )
  )
  val commands = div(execCancelButton, closeButton)

  def dateRep: String = Misc.formatAppointDate(appointTime.date)
  def timesRep: String = Misc.formatAppointTimeSpan(appointTime)
