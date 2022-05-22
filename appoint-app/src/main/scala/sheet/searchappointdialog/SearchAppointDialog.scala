package dev.myclinic.scala.web.appoint.sheet.searchappointdialog

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Appoint
import dev.myclinic.scala.model.AppointTime
import dev.fujiwara.domq.searchform.SearchForm
import dev.fujiwara.kanjidate.KanjiDate
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}

object SearchAppointDialog:
  type Result = (Appoint, AppointTime)

  def open(): Unit =
    val searchForm = SearchForm[Result, Result](
      toLabel _,
      identity,
      search _
    )
    val dlog = new ModalDialog3()
    dlog.title(innerText := "予約検索")
    dlog.body(searchForm.ele(cls := "appoint-search-appoint-body"))
    dlog.commands(
      button("閉じる", onclick := (_ => dlog.close()))
    )
    dlog.open()

  def toLabel(r: Result): String =
    r match {
      case (appoint, appointTime) =>
        val d = KanjiDate.dateToKanji(appointTime.date)
        val t = KanjiDate.timeToKanji(appointTime.fromTime)
        s"${appoint.patientName} (${appoint.patientId}) ${d} ${t} ${appoint.memo}"
    }

  def search(text: String): Future[List[Result]] =
    val pat = "[ 　]+".r
    val parts = pat.split(text)
    if parts.size == 1 then
      Api.searchAppointByPatientName(parts(0))
    else if parts.size == 2 then
      Api.searchAppointByPatientName2(parts(0), parts(1))
    else Future.successful(List.empty)

