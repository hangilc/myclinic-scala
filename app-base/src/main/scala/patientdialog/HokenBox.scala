package dev.myclinic.scala.web.appbase.patientdialog

import dev.myclinic.scala.apputil.HokenUtil

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.ShahokokuhoReps
import dev.myclinic.scala.web.appbase.KoukikoureiReps
import dev.myclinic.scala.web.appbase.RoujinReps
import dev.myclinic.scala.web.appbase.KouhiReps
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import scala.util.Success
import scala.util.Failure
import org.scalajs.dom.HTMLElement

case class HokenBox(
    hoken: Hoken,
    countMaps: (Map[Int, Int], Map[Int, Int], Map[Int, Int], Map[Int, Int]),
    onEdit: Hoken => Unit,
    onDelete: Hoken => Unit
):
  val (
    shahokokuhoCountMap,
    koukikoureiCountMap,
    roujinCountMap,
    kouhiCountMap
  ) = countMaps
  val repSpan = span
  val detailSpan = span
  val errBox = ErrorBox()
  val ele = div(
    cls := "reception-hoken-box",
    cls := Hoken.codeOf(hoken),
    repSpan(HokenUtil.hokenRep(hoken)),
    detailSpan(innerText := detail),
    a("編集", onclick := (() => onEdit(hoken))),
    (if getCount(hoken) == 0 then Some(a("削除", onclick := (() => doOnDelete())))
    else None),
    errBox.ele
  )

  def hokenKind: Hoken.HokenKind = Hoken.HokenKind(hoken)

  def doOnDelete(): Unit =
    ShowMessage.confirm("この保険を削除していいですか？")(() => {
      (hoken match {
        case h: Shahokokuho => Api.deleteShahokokuho(h.shahokokuhoId)
        case h: Koukikourei => Api.deleteKoukikourei(h.koukikoureiId)
        case h: Roujin      => Api.deleteRoujin(h.roujinId)
        case h: Kouhi       => Api.deleteKouhi(h.kouhiId)
      }).onComplete {
        case Success(_) => onDelete(hoken)
        case Failure(ex) => errBox.show(ex.toString)
      }
    })

  def getCount(hoken: Hoken): Int =
    hoken match {
      case h: Shahokokuho => shahokokuhoCountMap(h.shahokokuhoId)
      case h: Koukikourei => koukikoureiCountMap(h.koukikoureiId)
      case h: Roujin      => roujinCountMap(h.roujinId)
      case h: Kouhi       => kouhiCountMap(h.kouhiId)
    }

  def detail: String =
    hoken match {
      case h: Shahokokuho =>
        pairsToDetail(new ShahokokuhoReps(Some(h)).detailPairs, getCount(hoken))
      case h: Koukikourei =>
        pairsToDetail(new KoukikoureiReps(Some(h)).detailPairs, getCount(hoken))
      case h: Roujin =>
        pairsToDetail(new RoujinReps(Some(h)).detailPairs, getCount(hoken))
      case h: Kouhi =>
        pairsToDetail(new KouhiReps(Some(h)).detailPairs, getCount(hoken))
    }

  def pairToDetailItem(pair: (String, String)): String =
    val (label, value) = pair
    s"【${label}】${value}"

  def pairsToDetail(
      pairs: List[(String, String)],
      count: Int
  ): String =
    (pairs :+ ("使用回数", count.toString)).map(pairToDetailItem).mkString("")

object HokenBox:
  given Ordering[HokenBox] =
    Ordering
      .by[HokenBox, LocalDate](box => HokenUtil.validFromOf(box.hoken))
      .reverse

  given Comp[HokenBox] = _.ele

  given Dispose[HokenBox] = _ => ()
