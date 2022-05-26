package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate

case class ShinryouMenu(at: LocalDate, visitId: Int):
  val auxMenu = PullDownLink("その他")
  auxMenu.setBuilder(auxMenuItems)

  val ele = div(
    a("[診療行為]", onclick := (doRegular _)),
    auxMenu.ele
  )

  def auxMenuItems: List[(String, () => Unit)] =
    List(
      "検査" -> (() => ()),
      "検索入力" -> (() => ()),
      "重複削除" -> (() => ()),
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
