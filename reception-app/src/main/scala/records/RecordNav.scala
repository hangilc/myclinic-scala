package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import java.time.{LocalDate, LocalDateTime}
import org.scalajs.dom.raw.{HTMLElement}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.apputil.DrugUtil
import dev.fujiwara.dateinput.ZenkakuUtil
import dev.myclinic.scala.{util => ju}
import dev.myclinic.scala.util.NumberUtil

class RecordNav(onChange: Int => Unit = _ => ()):
  var total = 0
  var page = 0
  val eDisp = span()
  val ele = div(
    a("最初", onclick := (() => gotoPage(0))),
    a("前へ", onclick := (() => gotoPage(page - 1))),
    a("次へ", onclick := (() => gotoPage(page + 1))),
    a("最後", onclick := (() => gotoPage(total - 1))),
    eDisp,
  )
  updateDisp()

  def setTotal(value: Int): Unit =
    total = value

  def setPage(value: Int): Unit = 
    page = value
    updateDisp()

  private def gotoPage(p: Int): Unit =
    if p >= 0 && p < total && p != page then
      onChange(p)
      page = p
      updateDisp()

  private def updateDisp(): Unit =
    if total > 1 then
      eDisp.innerText = s"[${page+1}/${total}]"