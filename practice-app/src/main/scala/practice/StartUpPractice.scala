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
    given InitNoneConverter with
      def convert: Option[LocalDate] = Some(LocalDate.of(1957, 6, 2))
    service.right.ui.ele(
      div(DateOptionInput().ele),
      div(DateInput(LocalDate.now()).ele)
    )
    Future.successful(())

