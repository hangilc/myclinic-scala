package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.disease.Frame
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus

object Disease:
  val ele = div()

  PracticeBus.patientStartingSubscriberChannel.subscribe(s => {
    val frame = Frame(s.patient.patientId)
    frame.current()
    ele(clear, frame.ele)

  })
  PracticeBus.patientClosingSubscriberChannel.subscribe(s => {
    ele(clear)
  })

