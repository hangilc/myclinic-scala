package dev.myclinic.scala.web.reception.scan.docscan

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Modifiers
import dev.fujiwara.domq.InPlaceEdit
import dev.fujiwara.domq.TypeClasses.*
import dev.fujiwara.domq.searchform.SearchForm
import dev.myclinic.scala.model.Patient
import dev.myclinic.scala.webclient.{Api, global}

class PatientRow:
  import PatientRow.*
  var onDataChange: Option[Patient] => Unit = _ => ()
  val disp = new PatientDisp
  val select = new PatientSelect
  val ipe = new InPlaceEdit(disp, select, None)
  ipe.edit()
  def ele = ipe.ele
  ipe.onDataChange = patientOpt => onDataChange(patientOpt)

object PatientRow:
  class PatientDisp:
    var onEdit: () => Unit = () => ()
    val disp = span
    val cancelLink = a()
    val row = new Row
    row.title("患者")
    row.content(
      disp,
      cancelLink(innerText := "[変更]", onclick := (() => onEdit()))
    )
    def ele = row.ele
    def set(patient: Patient): Unit =
      disp(innerText := format(patient))
      cancelLink(displayDefault)
    def clear(): Unit =
      disp(Modifiers.clear, "（選択されていません）")
      cancelLink(innerText := "[選択]")
    def format(patient: Patient): String =
      String.format("(%d) %s", patient.patientId, patient.fullName())

  object PatientDisp:
    given ElementProvider[PatientDisp] = _.ele
    given DataAcceptor[PatientDisp, Option[Patient]] with
      def setData(t: PatientDisp, d: Option[Patient]): Unit =
        d.fold(t.clear())(t.set(_))
    given TriggerProvider[PatientDisp] with
      def setTriggerHandler(t: PatientDisp, handler: () => Unit): Unit =
        t.onEdit = handler

  class PatientSelect:
    var onSelect: Patient => Unit = _ => ()
    var onCancel: () => Unit = () => ()
    val search = new SearchForm[Patient, Patient](
      identity,
      text => Api.searchPatient(text).map(_._2)
    )
    search.ui.selection.formatter = patient =>
      String.format("(%04d) %s", patient.patientId, patient.fullName())
    search.ui.form(a("[キャンセル]", onclick := (() => onCancel())))
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

  object PatientSelect:
    given ElementProvider[PatientSelect] = _.ele
    given DataAcceptor[PatientSelect, Option[Patient]] with
      def setData(t: PatientSelect, dOpt: Option[Patient]): Unit =
        t.search.ui.selection.clear()
        dOpt.foreach(d =>
          t.search.ui.input.value = s"${d.lastName} ${d.firstName}"
        )
    given DataProvider[PatientSelect, Option[Patient]] with
      def getData(t: PatientSelect): Option[Patient] =
        t.search.ui.selection.selected
    given TriggerProvider[PatientSelect] with
      def setTriggerHandler(t: PatientSelect, handler: () => Unit): Unit =
        t.onSelect = (_ => handler())
    given GeneralTriggerProvider[PatientSelect, "cancel"] with
      def setTriggerHandler(t: PatientSelect, handler: () => Unit): Unit =
        t.onCancel = handler
