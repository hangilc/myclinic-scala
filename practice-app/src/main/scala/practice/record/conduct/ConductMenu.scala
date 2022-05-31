package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate

case class ConductMenu(at: LocalDate, visitId: Int):
  val link = PullDownLink("処置")
  link.setBuilder(menuItems)
  val workarea = div
  val ele = div(link.ele, workarea)

  def menuItems: List[(String, () => Unit)] =
    List(
      "Ｘ線検査追加" -> (doXp _),
      "注射追加" -> (doConductDrug _),
      "全部コピー" -> (() => ()),
    )

  def doXp(): Unit =
    val w = XpWidget(at, visitId, _.close())
    workarea.prepend(w.ele)

  def doConductDrug(): Unit =
    val w = ConductDrugWidget(at, visitId, _.close())
    workarea.prepend(w.ele)

