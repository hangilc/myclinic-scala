package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}

class RecordsWrapper:
  val ele = div()
  PracticeBus.patientChanged.subscribe(patientOpt => patientOpt match {
    case Some(patient) => ()
    case None => ele(clear)
  })

