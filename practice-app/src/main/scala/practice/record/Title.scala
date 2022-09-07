package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDateTime
import dev.fujiwara.kanjidate.KanjiDate
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.web.practiceapp.practice.NavUtil
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import dev.myclinic.scala.practiceapp.practice.record.title.RcptDetailDialog
import dev.myclinic.scala.web.practiceapp.practice.record.title.FutanwariDialog
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus

class Title(visit: VisitEx):
  import Title as Helper
  def at: LocalDateTime = visit.visitedAt
  val unsubscribers = List(
    PracticeBus.tempVisitIdChangedSubscriberChannel.subscribe(
      adaptToTempVisitId _
    )
  )
  val pullDown = PullDown.pullDownLink(
    "操作",
    List(
      "この診察を削除" -> (doDeleteVisit _),
      "暫定診察に設定" -> (setTempVisitId _),
      "暫定診察の解除" -> (() => {
        PracticeBus.tempVisitController.clearTempVisitId()
      }),
      "診療明細" -> (doRcptDetail _),
      "負担割オーバーライド" -> (doFutanwari _),
      "未収リストへ" -> (doAddToMishuu _)
    )
  )(cls := "practice-visit-title-pulldown")

  val ele = div(
    cls := "practice-visit-title",
    span(
      cls := "practice-visit-title-date",
      innerText := Helper.formatVisitTime(at)
    ),
    pullDown(cls := "practice-visit-title-manip")
  )

  adaptToTempVisitId(PracticeBus.currentTempVisitId)

  def doAddToMishuu(): Unit =
    for
      patient <- Api.getPatient(visit.patientId)
      meisai <- Api.getMeisai(visit.visitId)
    yield PracticeBus.addMishuu(visit.toVisit, patient, meisai)

  def doFutanwari(): Unit =
    val dlog = FutanwariDialog(
      visit.toVisit,
      dlog =>
        dlog.close()
        for vex <- Api.getVisitEx(visit.visitId)
        yield PracticeBus.visitUpdated.publish(vex)
    )
    dlog.open()

  def doRcptDetail(): Unit =
    for meisai <- Api.getMeisai(visit.visitId)
    yield
      val dlog = RcptDetailDialog(meisai)
      dlog.open()

  def doDeleteVisit(): Unit =
    ShowMessage.confirm("この診察を削除しますか？")(() =>
      val visitId = visit.visitId
      Api.deleteVisit(visitId).onComplete {
        case Success(_) =>
          (PracticeBus.currentVisitId, PracticeBus.currentTempVisitId) match {
            case (Some(currentVisitId), None) if currentVisitId == visitId =>
              PracticeBus.patientStateController.startPatient(visit.patient, None,
                _.copy(visitId = None))
            case (None, Some(currentTempVisitId)) if currentTempVisitId == visitId => 
              PracticeBus.tempVisitController.clearTempVisitId()
              NavUtil.refreshNavSetting()
            case _ => NavUtil.refreshNavSetting()
          }
        case Failure(ex) => ShowMessage.showError(ex.getMessage)
      }
    )

  def setTempVisitId(): Unit =
    if !PracticeBus.tempVisitController.setTempVisitId(visit.visitId) then
      ShowMessage.showError("暫定診察を設定できませんでした。")

  def adaptToTempVisitId(optTempVisitId: Option[Int]): Unit =
    optTempVisitId match {
      case None => ele(cls :- "temp-visit")
      case Some(visitId) =>
        if visitId == visit.visitId then ele(cls := "temp-visit")
        else ele(cls :- "temp-visit")
    }

object Title:
  def formatVisitTime(at: LocalDateTime): String =
    val p1 = KanjiDate.dateToKanji(
      at.toLocalDate,
      formatYoubi = info => s"（${info.youbi}）"
    )
    val p2 = KanjiDate.timeToKanji(at.toLocalTime)
    p1 + p2

  given Dispose[Title] =
    Dispose.nop[Title] + (_.unsubscribers)
