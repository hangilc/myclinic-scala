package dev.myclinic.scala.web.practiceapp.practice

import scala.concurrent.Future
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import java.time.LocalDate

object StartUpPractice:
  def run(service: PracticeService): Future[Unit] =
    import dev.fujiwara.domq.{Absolute}
    import org.scalajs.dom.document
    val dateForm = dev.fujiwara.dateinput.DateInputForm(Some(LocalDate.now()))
    service.right.ui.ele(dateForm.ele,
      div(button("入力", onclick := (() => {
        println(dateForm.validated)
      })))
    )

    Future.successful(())

