package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.fujiwara.domq.CompSortDataList
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.practice.mishuu.MishuuItem
import dev.myclinic.scala.web.practiceapp.practice.mishuu.MishuuItem.given

object Mishuu:
  val itemWrapper = div
  val compList = CompSortDataList[MishuuItem, (Visit, Meisai)](itemWrapper, MishuuItem.apply.tupled)
  val sumArea = div
  val ele = div(
    displayNone,
    cls := "practice-right-widget",
    div(cls := "title", "未収リスト"),
    div(cls := "body",
      itemWrapper,
      sumArea,
      div(
        button("領収書PDF"),
        button("会計済に"),
        a("閉じる")
      )
    )
  )

  PracticeBus.mishuuListChanged.subscribe(list => 
    println(("mishuu", list))
    if list.isEmpty then ele(displayNone)
    else 
      compList.sync(list.map(t => (t._1, t._3)))
      val sum = list.map(_._3.charge).sum
      updateSum(sum)
      ele(displayDefault)
  )

  def updateSum(value: Int): Unit =
    sumArea(innerText := s"合計 ${value}円")


