package dev.myclinic.scala.web.appoint.sheet

import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.ContextMenu
import dev.fujiwara.domq.Modal
import dev.fujiwara.domq.Form
import dev.fujiwara.domq.ShowMessage
import org.scalajs.dom.raw.MouseEvent
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.{document, window}
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import dev.myclinic.scala.validator.AppointTimeValidator
import dev.myclinic.scala.web.appoint.Misc
import cats.data.ValidatedNec
import cats.data.Validated.{validNec, invalidNec, condNec, Valid, Invalid}
import dev.myclinic.scala.validator.Validators
import java.time.LocalTime
import dev.myclinic.scala.util.DateTimeOrdering.{*, given}
import scala.math.Ordered.orderingToOrdered

class AdminAppointTimeBox(appointTime: AppointTime)
    extends AppointTimeBox(appointTime):
  ele.addEventListener(
    "contextmenu",
    (event: MouseEvent) => {
      event.preventDefault
      ContextMenu(
        "Convert" -> doConvert,
        "Combine" -> doCombine,
        "Split" -> doSplit,
        "削除" -> doDelete,
      ).open(event)
    }
  )

  def doConvert(): Unit =
    ConvertAppointTimeDialog(appointTime).open()

  def doCombine(): Unit =
    for
      appointTimes <- Api.listAppointTimesForDate(appointTime.date)
    yield {
      CombineAppointTimesDialog(appointTime, appointTimes).open()
    }

  def doSplit(): Unit = 
    SplitAppointTimeDialog(appointTime).open()

  def doDelete(): Unit =
    val msg = "本当に削除しますか？"
    ShowMessage.confirm(msg, yes => {
      if yes then
        Api.deleteAppointTime(appointTime.appointTimeId)
    })





