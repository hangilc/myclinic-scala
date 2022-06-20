package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.dateinput.DateInput

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    import dev.fujiwara.domq.{Absolute}
    import org.scalajs.dom.document
    val dateInput = DateInput()
    service.right.ui.ele(dateInput.ele)
    Future.successful(())

