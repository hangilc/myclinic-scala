package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}

object Disease:
  val ele = div(
    displayNone,
    "病名"
  )

  PracticeBus.patientVisitChanged.subscribe { state =>
    PracticeBus.currentPatient match {
      case None => ele(displayNone)
      case Some(patient) => 
        for
          list <- Api.listCurrentDiseaseEx(patient.patientId)
        yield 
          println(list)
          ele(displayDefault)
    }
  }

