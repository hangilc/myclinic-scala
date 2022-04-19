package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus

class Nav:
  var total = 0
  var page = 0
  val eDisp = span()
  val ele = div(displayNone, cls := "practice-nav",
    a("最初", onclick := (() => gotoPage(0))),
    a("前へ", onclick := (() => gotoPage(page - 1))),
    a("次へ", onclick := (() => gotoPage(page + 1))),
    a("最後", onclick := (() => gotoPage(total - 1))),
    eDisp,
  )
  updateDisp()

  val unsubscribers = List(
    PracticeBus.navSettingChanged.subscribe {
      case (newPage, newTotal) => 
        println(("nav-setting", newPage, newTotal))
        setTotal(newTotal)
        setPage(newPage)
        updateDisp()
    },
    PracticeBus.navPageChanged.subscribe(newPage => 
      setPage(newPage)
      updateDisp()
    )
  )

  def setTotal(value: Int): Unit =
    total = value

  def setPage(value: Int): Unit = 
    page = value

  private def gotoPage(p: Int): Unit =
    if p >= 0 && p < total && p != page then
      PracticeBus.navPageChanged.publish(p)
      page = p
      updateDisp()

  private def updateDisp(): Unit =
    if total > 1 then
      eDisp(innerText := s"[${page+1}/${total}]")
      ele(displayDefault)
    else
      ele(displayNone)