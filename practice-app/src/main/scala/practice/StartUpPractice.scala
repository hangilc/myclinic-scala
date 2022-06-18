package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    val e = div(
      dev.fujiwara.dateinput.DateInput(Some(java.time.LocalDate.now())).ele
    )
    service.right.ui.ele(e)
    Future.successful(())

