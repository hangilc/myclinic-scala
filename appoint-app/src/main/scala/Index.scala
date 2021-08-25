package dev.myclinic.web

import dev.myclinic.scala.model._
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.Api
import dev.myclinic.scala.web.Binding._
import dev.myclinic.scala.web.Bs
import dev.myclinic.scala.web.Dialog
import dev.myclinic.scala.web.Implicits._
import dev.myclinic.scala.web.Modifiers._
import dev.myclinic.scala.web.html._
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import dev.fujiwara.domq.{Template, Traverse, GenId}

import java.time.LocalDate
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue

object JsMain {
  def main(args: Array[String]): Unit = {
    val body = document.body
    for(i <- 1 to 10){
      println(GenId.genId())
    }
    // body(cls := "px-5 pt-1 pb-5")
    // body.appendChild(banner)
    // body.appendChild(AppointRow.ele)
    // val startDate = DateUtil.startDayOfWeek(LocalDate.now())
    // val endDate = startDate.plusDays(6)
    // val api = Api
    // for (apps <- api.listAppoint(startDate, endDate)) {
    //   val cols = AppointDate.classify(apps).map(AppointColumn)
    //   cols.foreach(AppointRow.add)
    // }
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
    div(cls := "row mx-0", cb := { e => { eRow = e }})
  )

  def add(c: AppointColumn) {
    eRow.appendChild(c.ele)
  }
}

case class AppointColumn(appointDate: AppointDate) {
  
  var eDateRep, eSlots: Element = _
  val ele = div(cls := "col-2")(
    div(cb := (eDateRep = _)),
    div(cb := (eSlots = _)),
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
    div(cb := (eDetail = _)),
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
    val patientNameInput = InputBinding()
    val patientNameError = TextBinding()
    val dlog = Dialog[Int]("診察予約")
    dlog.open()
    dlog.content(
      div(dateTimeRep),
      form(
        div(cls := "row")(
          div(cls := "col-auto")(
            label(cls := "form-label")("患者名")
          ),
          div(cls := "col-auto")(
            input(attr("type") := "text", cls := "form-control",
              bindTo(patientNameInput)),
            div(cls := "invalid-feedback", bindTo(patientNameError))         )
        )
      )
    )
    dlog.commands(
      button(Bs.btn("btn-secondary"), Dialog.closeButton)("キャンセル"),
      button(Bs.btn("btn-primary"), onclick := onEnterClick)("予約する")
    )
    patientNameInput.value = "清水"
    dlog.onClosed(println(_))
    println("opened")

    def onEnterClick(): Unit = {
      val ok = validatePatientNameInput()
      if( ok ){
        dlog.close()
      }
    }

    def validatePatientNameInput(): Boolean = {
      val s = patientNameInput.value
      if( s.isEmpty ){
        patientNameError.text = "患者名が入力されていません。"
        patientNameInput.setValid(false)
      } else {
        patientNameInput.setValid(true)
      }
    }
  }

}
