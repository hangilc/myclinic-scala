package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.apputil.HokenUtil

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class HokenBox(hoken: Hoken):
  val workarea = div
  val ele = div(
    cls := "reception-cashier-hoken-box",
    span(HokenUtil.hokenRep(hoken), onclick := (onRepClick _)),
    workarea
  )

  def onRepClick(): Unit =
    if workarea.isDisplayed then
      ()
    else workarea.hide()

object HokenBox:
  given Ordering[HokenBox] = Ordering.by(box => HokenUtil.validFromOf(box.hoken))

  given Comp[HokenBox] = _.ele

  given Dispose[HokenBox] = _ => ()