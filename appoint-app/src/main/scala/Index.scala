package dev.myclinic.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.Api
import dev.myclinic.scala.web.appoint.MakeAppointDialog
import dev.fujiwara.domq.Binding._
import dev.fujiwara.domq.Dialog
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import io.circe.Encoder
import io.circe.syntax._

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

case class UpdateAppointArg(from: Appoint, to: Appoint)
object UpdateAppointArg {
  import io.circe.generic.semiauto._
  implicit val argEncoder: Encoder[UpdateAppointArg] = deriveEncoder
}

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    body(cls := "px-5 pt-1 pb-5")
    body.appendChild(banner)
    body.appendChild(AppointRow.ele)
    val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    val endDate = startDate.plusDays(6)
    for (apps <- Api.listAppoint(startDate, endDate)) {
      val cols = AppointDate.classify(apps).map(AppointColumn)
      cols.foreach(AppointRow.add)
      for(app <- apps){
        val arg = UpdateAppointArg(app, app)
        println(arg.asJson)
      }
    }
  }

  val banner = div(cls := "container-fluid")(
    div(cls := "row pt-3 pb-2 ml-5 mr-5")(
      h1(cls := "bg-dark text-white p-3 col-md-12")("診察予約")
    )
  )

}

case class AppointDate(date: LocalDate, appoints: List[Appoint])

object AppointDate {
  def classify(appList: List[Appoint]): List[AppointDate] = {
    val map = appList.groupBy(_.date)
    val result = for (k <- map.keys) yield AppointDate(k, map(k))
    result.toList.sortBy(_.date)
  }
}

object AppointRow {

  var eRow: Element = _

  val ele = div(cls := "container px-0 mx-0")(
    div(cls := "row mx-0", cb := { e => { eRow = e } })
  )

  def add(c: AppointColumn): Unit = {
    eRow.appendChild(c.ele)
  }
}

case class AppointColumn(appointDate: AppointDate) {

  var eDateRep, eSlots: Element = _
  val ele = div(cls := "col-2")(
    div(cb := (eDateRep = _)),
    div(cb := (eSlots = _))
  )

  eDateRep.innerText = dateRep
  appointDate.appoints.foreach(a => {
    val s = new SlotRow(a)
    eSlots.appendChild(s.ele)
  })

  def dateRep: String = {
    val d: LocalDate = appointDate.date
    val month = d.getMonthValue()
    val day = d.getDayOfMonth()
    s"${month}月${day}日"
  }
}

case class SlotRow(appoint: Appoint) {

  var eTime, eDetail: Element = _
  val ele = div(style := "cursor: pointer")(
    div(cb := (eTime = _)),
    div(cb := (eDetail = _))
  )

  eTime.innerText = appoint.time.toString()
  eDetail.innerText = detail
  ele.onclick(openDialog _)

  def detail: String = {
    if (appoint.patientName.isEmpty) {
      "（空）"
    } else {
      appoint.patientName
    }
  }

  def dateTimeRep: String = {
    val d = appoint.date
    val t = appoint.time
    s"${d.getMonthValue()}月${d.getDayOfMonth()}日${t.getHour()}時${t.getMinute()}分"
  }

  def openDialog(): Unit = {
    MakeAppointDialog.open(appoint, name => {
      println("name", name)
    })
  }

  // def onDialogOrig(): Unit = {
  //   val patientNameInput = InputBinding()
  //   val patientNameError = TextBinding()
  //   val dlog = Dialog[Int]("診察予約")
  //   dlog.open()
  //   dlog.content(
  //     div(dateTimeRep),
  //     form(
  //       div(cls := "row")(
  //         div(cls := "col-auto")(
  //           label(cls := "form-label")("患者名")
  //         ),
  //         div(cls := "col-auto")(
  //           input(
  //             attr("type") := "text",
  //             cls := "form-control",
  //             bindTo(patientNameInput)
  //           ),
  //           div(cls := "invalid-feedback", bindTo(patientNameError))
  //         )
  //       )
  //     )
  //   )
  //   dlog.commands(
  //     button(
  //       attr("type") := "button",
  //       cls := "btn btn-secondary",
  //       Dialog.closeButton
  //     )(
  //       "キャンセル"
  //     ),
  //     button(
  //       attr("type") := "button",
  //       cls := "btn btn-primary",
  //       onclick := (_ => onEnterClick())
  //     )("予約する")
  //   )

  //   def onEnterClick(): Unit = {
  //     val ok = validatePatientNameInput()
  //     if (ok) {
  //       dlog.close()
  //     }
  //   }

  //   def validatePatientNameInput(): Boolean = {
  //     val s = patientNameInput.value
  //     if (s.isEmpty) {
  //       patientNameError.text = "患者名が入力されていません。"
  //       patientNameInput.setValid(false)
  //     } else {
  //       patientNameInput.setValid(true)
  //     }
  //   }
  // }

}
