package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}

case class Disp(shinryou: ShinryouEx):
  val ele = div(innerText := shinryou.master.name, onclick := (onClick _))

  def onClick(): Unit =
    ele.replaceBy(Edit(shinryou).ele)

