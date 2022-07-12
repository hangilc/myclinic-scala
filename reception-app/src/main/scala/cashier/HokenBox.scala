package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.apputil.HokenUtil

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.ShahokokuhoReps
import dev.myclinic.scala.web.appbase.KoukikoureiReps
import dev.myclinic.scala.web.appbase.RoujinReps
import dev.myclinic.scala.web.appbase.KouhiReps
import java.time.LocalDate

case class HokenBox(
    hoken: Hoken,
    countMaps: (Map[Int, Int], Map[Int, Int], Map[Int, Int], Map[Int, Int]),
    onEdit: Hoken => Unit
):
  val (
    shahokokuhoCountMap,
    koukikoureiCountMap,
    roujinCountMap,
    kouhiCountMap
  ) = countMaps
  val repSpan = span
  val detailSpan = span
  val ele = div(
    cls := "reception-hoken-box",
    cls := Hoken.codeOf(hoken),
    repSpan(HokenUtil.hokenRep(hoken)),
    detailSpan(innerText := detail),
    a("編集", onclick := (() => onEdit(hoken)))
  )

  def detail: String =
    hoken match {
      case h: Shahokokuho =>
        pairsToDetail(
          new ShahokokuhoReps(Some(h)).detailPairs,
          shahokokuhoCountMap(h.shahokokuhoId)
        )
      case h: Koukikourei =>
        pairsToDetail(
          new KoukikoureiReps(Some(h)).detailPairs,
          koukikoureiCountMap(h.koukikoureiId)
        )
      case h: Roujin =>
        pairsToDetail(
          new RoujinReps(Some(h)).detailPairs,
          roujinCountMap(h.roujinId)
        )
      case h: Kouhi =>
        pairsToDetail(
          new KouhiReps(Some(h)).detailPairs,
          kouhiCountMap(h.kouhiId)
        )
    }

  def pairToDetailItem(pair: (String, String)): String =
    val (label, value) = pair
    s"【${label}】${value}"

  def pairsToDetail(
      pairs: List[(String, String)],
      count: Int
  ): String =
    (pairs :+ ("使用回数", count.toString)).map(pairToDetailItem).mkString("、")

object HokenBox:
  given Ordering[HokenBox] =
    Ordering.by[HokenBox, LocalDate](box => HokenUtil.validFromOf(box.hoken)).reverse

  given Comp[HokenBox] = _.ele

  given Dispose[HokenBox] = _ => ()
