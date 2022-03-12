package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{*, given}
import dev.fujiwara.kanjidate.KanjiDate

class PatientSelection:
  val onSelect = LocalEventPublisher[Patient]

  private val selection = Selection[Patient](onSelect = onSelect.publish(_))

  var formatter: Patient => String = patient =>
    String.format("%04d %s （%s生）", patient.patientId, patient.fullName(),
      KanjiDate.dateToKanji(patient.birthday))

  def set(ps: List[Patient]): Unit = 
    selection.clear()
    selection.addItems(ps, formatter)

  def ele = selection.ele
