package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    val frame = Frame(5366)
    service.right.ele(frame.ele(displayDefault))
    frame.edit()
    Future.successful(())

