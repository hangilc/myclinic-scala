package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}

case class ConductMenu():
  val link = PullDownLink("処置")
  link.setBuilder(menuItems)
  val workarea = div
  val ele = div(link.ele, workarea)

  def menuItems: List[(String, () => Unit)] =
    List(
      "Ｘ線検査追加" -> (doXp _),
      "注射追加" -> (() => ()),
      "全部コピー" -> (() => ()),
    )

  def doXp(): Unit =
    val w = XpWidget()
    workarea.prepend(w.ele)

