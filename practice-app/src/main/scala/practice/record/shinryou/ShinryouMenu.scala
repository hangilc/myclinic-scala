package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import cats.syntax.all.*
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus

case class ShinryouMenu(at: LocalDate, visitId: Int):
  val auxMenu = PullDownLink("その他")
  auxMenu.setBuilder(auxMenuItems)

  val ele = div(
    a("[診療行為]", onclick := (doRegular _)),
    auxMenu.ele
  )

  def auxMenuItems: List[(String, () => Unit)] =
    List(
      "検査" -> (doKensa _),
      "検索入力" -> (doSearch _),
      "重複削除" -> (doDeleteDuplicate _),
      "全部コピー" -> (() => ())
    )

  def doRegular(): Unit =
    for regulars <- Api.getShinryouRegular()
    yield
      val dlog = new RegularDialog(
        regulars("left"),
        regulars("right"),
        regulars("bottom"),
        at,
        visitId
      )
      dlog.open

  def doKensa(): Unit =
    for config <- Api.getShinryouKensa()
    yield
      val dlog = KensaDialog(config, at, visitId)
      dlog.open

  def doSearch(): Unit =
    val dlog = SearchDialog(at, visitId)
    dlog.open

  def doDeleteDuplicate(): Unit =
    for
      shinryouList <- Api.listShinryouForVisit(visitId)
      (shinryouIds, _) = shinryouList.foldLeft(
        (Set.empty[Int], Set.empty[Int])
      ) { case ((shinryouIds, shinryoucodes), shinryou) =>
        val code = shinryou.shinryoucode
        if shinryoucodes.contains(code) then
          (shinryouIds + shinryou.shinryouId, shinryoucodes
        )
        else (shinryouIds, shinryoucodes + code)
      }
      _ <- shinryouIds.toList.map(Api.deleteShinryou(_)).sequence
    yield 
      shinryouList.foreach(PracticeBus.shinryouDeleted.publish(_))
