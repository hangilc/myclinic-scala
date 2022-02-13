package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appbase.{LocalEventPublisher}
import java.time.LocalDate

class TopMenu(private var startDate: LocalDate):
  val onDateSelected = new LocalEventPublisher[LocalDate]
  val prevMonth = button
  val prevWeek = button
  val thisWeek = a
  val nextWeek = button
  val nextMonth = button
  val topMenuBox = span()
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
      thirdShot("追加接種", mr := "14px"),
      menuIcon(Icons.defaultStyle)
    )
  )
  prevMonth(onclick := (() => advanceDays(-28)))
  prevWeek(onclick := (() => advanceDays(-7)))
  thisWeek(onclick := (onThisWeekClick _))
  nextWeek(onclick := (() => advanceDays(7)))
  nextMonth(onclick := (() => advanceDays(28)))
  thirdShot(onclick := (onThirdShotClick _))
  menuIcon(onclick := (onMenuClick _))

  def getStartDate: LocalDate = startDate
  def getDateRange: (LocalDate, LocalDate) = (startDate, startDate.plusDays(6))

  private def advanceDays(diff: Int): Unit =
    startDate = startDate.plusDays(diff)
    onDateSelected.publish(startDate)

  private def onThisWeekClick(): Unit =
    startDate = LocalDate.now()
    onDateSelected.publish(startDate)

  private def onThirdShotClick(): Unit = ()
  private def onMenuClick(): Unit = ()


