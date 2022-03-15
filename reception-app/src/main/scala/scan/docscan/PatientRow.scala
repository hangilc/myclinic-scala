package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.FlipFlop
import dev.fujiwara.domq.searchform.SearchForm
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}

class PatientSelect:
  var onSelect: Patient => Unit = _ => ()
  val search = new SearchForm[Patient, Patient](
    identity,
    text => Api.searchPatient(text).map(_._2)
  )
  search.ui.selection.formatter = patient =>
    String.format("(%04d) %s", patient.patientId, patient.fullName())
  search.ui.selection.hide()
  search.engine.onSearchDone = () => search.ui.selection.show()
  search.onSelect(patient => {
    search.ui.selection.hide()
    onSelect(patient)
  })
  val row = new Row
  row.title("患者選択")
  row.content(search.ele)
  def ele = row.ele

class PatientDisp:
  val disp = span
  val row = new Row
  row.title("患者")
  row.content(disp)
  def ele = row.ele
  def set(patient: Patient): Unit =
    disp(innerText := format(patient))
  def format(patient: Patient): String =
    String.format("(%d) %s", patient.patientId, patient.fullName())


class PatientRow:
  val select = new PatientSelect
  val disp = new PatientDisp
  val ff = new FlipFlop(select.ele, disp.ele)
  def ele = ff.ele
  select.onSelect = patient =>
    disp.set(patient)
    ff.flop()
    
