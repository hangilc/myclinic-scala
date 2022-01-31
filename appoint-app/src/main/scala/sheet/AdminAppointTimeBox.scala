package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.Form
import dev.fujiwara.domq.ShowMessage
import org.scalajs.dom.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.concurrent.Future
import dev.myclinic.scala.validator.AppointTimeValidator
import dev.myclinic.scala.web.appoint.Misc
import cats.data.ValidatedNec
import cats.data.Validated.{validNec, invalidNec, condNec, Valid, Invalid}
import dev.myclinic.scala.validator.Validators
import java.time.LocalTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

class AdminAppointTimeBox(
    _appointTime: AppointTime,
    _gen: Int,
    _findVacantFollowers: () => List[AppointTime]
) extends AppointTimeBox(_appointTime, _gen, _findVacantFollowers):
  ele(oncontextmenu := (onContextMenu _))

  def onContextMenu(event: MouseEvent): Unit =
    event.preventDefault()
    var menu: List[(String, () => Unit)] = List(
      "編集" -> doConvert,
      "結合" -> doCombine,
      "分割" -> doSplit,
      "延長" -> doExtend,
    )
    if slots.isEmpty then menu = menu :+ ("削除" -> doDelete)
    ContextMenu(menu).open(event)

  def doConvert(): Unit =
    ConvertAppointTimeDialog(appointTime).open()

  def doCombine(): Unit =
    CombineAppointTimesDialog(appointTime, findVacantFollowers()).open()

  def doSplit(): Unit =
    SplitAppointTimeDialog(appointTime).open()

  def doDelete(): Unit =
    val msg = "本当に削除しますか？"
    ShowMessage.confirm(msg)(() => {
      Api.deleteAppointTime(appointTime.appointTimeId)
      ()
    })

  def doExtend(): Unit =
    val dlog = ExtendAppointTimeDialog(appointTime)
    dlog.open()
