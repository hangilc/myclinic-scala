package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.searchform.SearchForm
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}

class PatientSelect:
  var onSelect: Patient => Unit = _ => ()
  val search = new SearchForm[Patient, Patient](identity, text => Api.searchPatient(text).map(_._2))
  search.ui.selection.formatter = patient => String.format("(%04d) %s", patient.patientId, patient.fullName())
  search.ui.selection.hide()
  search.engine.onSearchDone = () => search.ui.selection.show()
  search.onSelect(patient => {
    onSelect(patient)
    search.ui.selection.hide()
  })
  val ele = div(
    div("患者選択", cls := "doc-scan-subtitle"),
    search.ele(cls := "doc-scan-subcontent")
  )


