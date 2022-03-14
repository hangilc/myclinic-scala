package dev.myclinic.scala.appbase

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.appbase.LocalEventPublisher
import dev.myclinic.scala.model.Patient

class PatientSearch:
  val input = inputText
  val sel = Selections.patientSelection()
  val ele = div(
    div(
      input(cls := "appbase-patient-search-input"),
      button("検索", onclick := (doSearch _))
    ),
    sel.ele
  )
  val onSelect = LocalEventPublisher[Patient]
  sel.onSelect = value => onSelect.publish(value)
  def selected: Option[Patient] = sel.selected

  private def doSearch(): Unit =
    val txt = input.value.trim
    if !txt.isEmpty then
      for
        (gen, patients) <- Api.searchPatient(txt)
      yield
        sel.clear()
        sel.addAll(patients)
          

