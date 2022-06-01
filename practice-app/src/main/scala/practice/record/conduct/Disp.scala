package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.all.{*, given}

case class Disp(c: ConductEx):
  val ele = div(innerText := ConductHelper.rep(c), onclick := (onClick _))

  def onClick(): Unit = 
    ele.replaceBy(Edit(c).ele)