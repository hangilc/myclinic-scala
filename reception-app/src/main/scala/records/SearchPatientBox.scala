package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.Patient

class SearchPatientBox(cb: Patient => Unit):
  val searchText = inputText()
  val selection = Selection[Patient](onSelect = cb)
  val ele = div(cls := "records-search-patient-box")(
    div("患者検索", cls := "title"),
    form(onsubmit := (onSearch _))(
      searchText,
      button("検索", attr("type") := "submit")
    ),
    selection.ele
  )

  def onSearch(): Unit =
    val txt = searchText.value.trim
    selection.clear()
    for
      (gen, patients) <- Api.searchPatient(txt)
    yield {
      searchText.value = ""
      patients.foreach(patient => {
        val label = String.format("(%04d) %s", patient.patientId, patient.fullName(""))
        selection.add(div(innerText := label), patient)
      })
    }




