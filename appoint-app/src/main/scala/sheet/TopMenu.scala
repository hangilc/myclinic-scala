package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.myclinic.scala.web.appoint.sheet.covidthirdshot.CovidThirdShot
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appoint.AppointHistoryWindow
import scala.util.{Success, Failure}
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.util.DateUtil
import dev.myclinic.scala.web.appoint.sheet.searchappointdialog.SearchAppointDialog
import scala.language.implicitConversions

class TopMenu(private var startDate: LocalDate):
  val onDateSelected = new LocalEventPublisher[LocalDate]
  val prevMonth = button
  val prevWeek = button
  val thisWeek = a
  val nextWeek = button
  val nextMonth = button
  val topMenuBox = span()
  val searchAppointLink = a
  val thirdShot = a
  val menuIcon = Icons.menu

  val ele = div(
    mb := "0.5rem",
    textAlign := "center",
  )(
    prevMonth("前の月"),
    prevWeek("前の週"),
    thisWeek("今週", href := "", ml := "0.5rem"),
    nextWeek("次の週", ml := "0.5rem"),
    nextMonth("次の月", ml := "0.5rem"),
    topMenuBox(attr("id") := "top-menu-box")(
      searchAppointLink("予約検索"),
      thirdShot("追加接種", mr := "14px"),
      menuIcon(Icons.defaultStyle)
    )
  )
  prevMonth(onclick := (() => advanceDays(-28)))
  prevWeek(onclick := (() => advanceDays(-7)))
  thisWeek(onclick := (onThisWeekClick _))
  nextWeek(onclick := (() => advanceDays(7)))
  nextMonth(onclick := (() => advanceDays(28)))
  searchAppointLink(onclick := (TopMenu.openSearchAppointDialog _))
  thirdShot(onclick := (TopMenu.openThirdShotDialog _))
  menuIcon(onclick := (onMenuClick _))

  def getStartDate: LocalDate = startDate
  def getDateRange: (LocalDate, LocalDate) = (startDate, startDate.plusDays(6))

  private def advanceDays(diff: Int): Unit =
    startDate = startDate.plusDays(diff)
    onDateSelected.publish(startDate)

  private def onThisWeekClick(): Unit =
    startDate = DateUtil.startDayOfWeek(LocalDate.now())
    onDateSelected.publish(startDate)

  private def onThirdShotClick(): Unit = ()
  private def onMenuClick(event: MouseEvent): Unit =
    ContextMenu(List("変更履歴" -> (TopMenu.showHistory _))).open(event)

object TopMenu:
  def openThirdShotDialog(): Unit =
    val content = CovidThirdShot()
    val w =
      FloatWindow("追加接種", content.ui.ele(padding := "10px"), width = "300px")
    w.open()
    content.initFocus()

  def showHistory(): Unit =
    val f =
      for
        events <- Api.listAppointEvents(30, 0)
        _ <- AppointHistoryWindow.open(events)
      yield ()
    f.onComplete {
      case Success(_)  => ()
      case Failure(ex) => ShowMessage.showError(ex.getMessage)
    }

  def openSearchAppointDialog(): Unit =
    SearchAppointDialog.open()



