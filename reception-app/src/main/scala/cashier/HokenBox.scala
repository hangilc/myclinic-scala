package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.apputil.HokenUtil

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.ShahokokuhoReps
import dev.myclinic.scala.web.appbase.KoukikoureiReps
import dev.myclinic.scala.web.appbase.RoujinReps
import dev.myclinic.scala.web.appbase.KouhiReps

case class HokenBox(hoken: Hoken):
  val repSpan = span
  val detailSpan = span
  val workarea = div
  val ele = div(
    cls := "reception-hoken-box",
    cls := "reception-cashier-hoken-box",
    repSpan(HokenUtil.hokenRep(hoken), onclick := (onRepClick _)),
    detailSpan(innerText := detail),
    workarea
  )

  def onRepClick(): Unit =
    if workarea.isDisplayed then
      ()
    else workarea.hide()

  def detail: String = 
    hoken match {
      case h: Shahokokuho => pairsToDetail(new ShahokokuhoReps(Some(h)).detailPairs)
      case h: Koukikourei => pairsToDetail(new KoukikoureiReps(Some(h)).detailPairs)
      case h: Roujin => pairsToDetail(new RoujinReps(Some(h)).detailPairs)
      case h: Kouhi => pairsToDetail(new KouhiReps(Some(h)).detailPairs)
    }
  
  def pairToDetailItem(pair: (String, String)): String =
    val (label, value) = pair
    s"【${label}】${value}"

  def pairsToDetail(pairs: List[(String, String)]): String =
    pairs.map(pairToDetailItem).mkString("、")

object HokenBox:
  given Ordering[HokenBox] = Ordering.by(box => HokenUtil.validFromOf(box.hoken))

  given Comp[HokenBox] = _.ele

  given Dispose[HokenBox] = _ => ()