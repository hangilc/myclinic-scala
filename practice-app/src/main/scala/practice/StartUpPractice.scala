package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    import dev.fujiwara.domq.{Absolute, Viewport}
    val e = div(border := "10px solid orange")
    e.style.overflowY = "scroll"
    e.style.padding = "10px"
    e.style.margin = "10px"
    Absolute.positionAbsolute(e, 200, 120)
    org.scalajs.dom.document.body(e)

    val b = button("計算", onclick := (() =>
      println("Viewport")
      println(Viewport.width)  
      println(Viewport.height)  
      println(Viewport.offsetLeft)  
      println(Viewport.offsetTop)  
    ))
    service.right.ui.ele(b)
    val (left, top) = (Absolute.leftOf(b), Absolute.topOf(b))
    println((left, top))
    Absolute.setRightOf(e, left)
    Absolute.setBottomOf(e, top)
    Future.successful(())

