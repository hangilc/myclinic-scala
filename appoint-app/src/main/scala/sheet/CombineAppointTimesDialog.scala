package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.{Modal, Form, ErrorBox, ShowMessage}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api

class CombineAppointTimesDialog(head: AppointTime, appointTimes: List[AppointTime]):
  val dialog: Modal = Modal(
    "予約枠の結合",
    div(),
    div()
  )

  def open(): Unit = 
    val nFollows = 1
    val followers = listFollowers(head, appointTimes).take(nFollows)
    if followers.isEmpty then
      ShowMessage.showMessage("結合する予約枠がありません。")
    else
      makeDialog().open()

  def makeDialog(): Modal =
    Modal(
      "予約枠の結合",
      div(),
      div()
    )

  def listFollowers(head: AppointTime, appointTimes: List[AppointTime]): List[AppointTime] =
    appointTimes
      .dropWhile(_.appointTimeId != head.appointTimeId)
      .sliding(2)
      .takeWhile({
        case a :: b :: _ => a.isAdjacentTo(b)
        case _           => false
      })
      .map(_(1))
      .toList
