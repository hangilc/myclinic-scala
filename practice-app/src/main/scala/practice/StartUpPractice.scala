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
    import dev.fujiwara.dateinput.*
    service.right.ui.ele(
      button("click", onclick := (() =>
        val dlog = new DatePicker(None)
        dlog.open(_ => ())  
      ))
    )
    Future.successful(())

