package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    import dev.fujiwara.domq.{Absolute, Viewport}
    import org.scalajs.dom.document
    val b = button("計算")
    service.right.ui.ele(b)
    val e = div()
    e.id = "e"
    e.style.border = "10px solid orange"
    Absolute.positionAbsolute(e, 500, 400)
    Absolute.setLeftOf(e, Absolute.leftOf(b) + b.getBoundingClientRect().width)
    Absolute.setBottomOf(e, Absolute.topOf(b) + b.scrollHeight)
    Absolute.enableDrag(e, e)
    Absolute.openWithScreen(e, e => Absolute.ensureInViewFlipping(e, Absolute.leftOf(b), Absolute.topOf(b)))
    Future.successful(())

