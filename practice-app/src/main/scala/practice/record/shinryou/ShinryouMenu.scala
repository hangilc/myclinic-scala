package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}

case class ShinryouMenu():
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
      "全部コピー" -> (() => ()),
    )

  def doRegular(): Unit =
    val dlog = new RegularDialog()
    dlog.open
