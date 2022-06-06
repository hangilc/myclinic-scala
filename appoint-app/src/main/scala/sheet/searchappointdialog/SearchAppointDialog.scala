package dev.myclinic.scala.web.appoint.sheet.searchappointdialog

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Appoint
import dev.myclinic.scala.model.AppointTime
import dev.fujiwara.domq.searchform.SearchForm
import dev.fujiwara.kanjidate.KanjiDate
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appoint.sheet.AppointKind

object SearchAppointDialog:
  type Result = (Appoint, AppointTime)

  def open(): Unit =
    val searchForm = SearchForm[Result](
      toLabel _,
      search _
    )
    searchForm.onSelect(r =>
      r match {
        case (appoint, _) =>
          for
            byId <-
              if appoint.patientId > 0 then
                Api.searchAppointByPatientId(appoint.patientId)
              else Future.successful(List.empty)
            byName <- searchByName(appoint.patientName)
          yield 
            val list = (byId ++ byName).sortBy(r => (r._2.date, r._2.fromTime)).reverse.distinct
            searchForm.engine.setItems(list)
      }
    )
    val dlog = new ModalDialog3()
    dlog.title(innerText := "予約検索")
    dlog.body(searchForm.ele(cls := "appoint-search-appoint-body"))
    dlog.commands(
      button("閉じる", onclick := (_ => dlog.close()))
    )
    dlog.open()
    searchForm.initFocus()

  def toLabel(r: Result): String =
    r match {
      case (appoint, appointTime) =>
        val d = KanjiDate.dateToKanji(appointTime.date)
        val t = KanjiDate.timeToKanji(appointTime.fromTime)
        val k = AppointKind(appointTime.kind)
        val kind = k.code match {
          case "regular" => ""
          case _ => k.label
        }
        s"${appoint.patientName} (${appoint.patientId}) ${d} ${t} ${kind} ${appoint.memo}"
    }

  def search(text: String): Future[List[Result]] =
    val digits = raw"\d+".r
    if digits.matches(text) then
      val patientId = text.toInt
      Api.searchAppointByPatientId(patientId)
    else searchByName(text)

  def searchByName(text: String): Future[List[Result]] =
    val pat = "[ 　]+".r
    val parts = pat.split(text)
    if parts.size == 1 && !parts(0).isEmpty then
      Api.searchAppointByPatientName(parts(0))
    else if parts.size == 2 then
      Api.searchAppointByPatientName2(parts(0), parts(1))
    else Future.successful(List.empty)
