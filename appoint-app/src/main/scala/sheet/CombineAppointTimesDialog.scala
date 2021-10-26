package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.{Modal, Form, ErrorBox, ShowMessage}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.webclient.Api
import dev.myclinic.scala.web.appoint.Misc

class CombineAppointTimesDialog(
    head: AppointTime,
    appointTimes: List[AppointTime]
):

  def open(): Unit =
    val nFollows = 1
    val followers = listFollowers(head, appointTimes).take(nFollows)
    if followers.isEmpty then ShowMessage.showMessage("結合する予約枠がありません。")
    else makeDialog(followers).open()

  def makeDialog(followers: List[AppointTime]): Modal =
    val execButton = Modal.execute
    val cancelButton = Modal.cancel
    val dialog = Modal(
      "予約枠の結合",
      div(innerText := makeText(head, followers)),
      div(execButton, cancelButton)
    )
    execButton(onclick := (() => { onExec(followers); dialog.close() }))
    cancelButton(onclick := (() => dialog.close()))
    dialog

  def onExec(followers: List[AppointTime]): Unit =
    val ids = (head :: followers).map(_.appointTimeId)
    Api.combineAppointTimes(ids)

  def listFollowers(
      head: AppointTime,
      appointTimes: List[AppointTime]
  ): List[AppointTime] =
    appointTimes
      .dropWhile(_.appointTimeId != head.appointTimeId)
      .sliding(2)
      .takeWhile({
        case a :: b :: _ => a.isAdjacentTo(b)
        case _           => false
      })
      .map(_(1))
      .toList

  def makeText(head: AppointTime, followers: List[AppointTime]): String =
    List(
      "以下のように予約枠を結合します。",
      Misc.formatAppointDate(head.date),
      Misc.formatAppointTime(head.fromTime) + " - ",
      Misc.formatAppointTime(followers.last.untilTime)
    ).mkString("\n")
