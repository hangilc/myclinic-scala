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
    patientName: => String,
    patientId: => Int,
    memo: => String
):
  val execCancelButton = button("予約取消実行")
  val closeButton = button("閉じる")
  private val patientIdPart = PatientIdPart(patientId, appointId, patientName)
  private val memoPart = MemoPart(memo, appointId)
  val body = div(
    div(timesRep),
    div(patientName),
    Form.rows(
      patientIdPart.keyPart -> patientIdPart.valuePart,
      memoPart.keyPart -> memoPart.valuePart,
    )
  )
  val commands = div(execCancelButton, closeButton)

  def dateRep: String = Misc.formatAppointDate(appointTime.date)
  def timesRep: String = Misc.formatAppointTimeSpan(appointTime)

  def onPatientIdChanged(): Unit = 
    patientIdPart.onPatientIdChanged(patientId)
  def onMemoChanged(): Unit =
    memoPart.onMemoChanged(memo)
