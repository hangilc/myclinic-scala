package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.Selection
import dev.myclinic.scala.model.{Patient, Sex}
import java.time.LocalDate

class ScanBox:
  val eSearchResult: Selection[Patient] = Selection[Patient]()
  val eSelectedPatient: HTMLElement = div()
  val eScanned: HTMLElement = div()
  val ele = div(cls := "scan-box")(
    div(cls := "search-area")(
      h2("患者選択"),
      form(
        inputText(),
        button(attr("type") := "default")("検索")
      )
    ),
    eSearchResult.ele(cls := "search-result", displayNone),
    eSelectedPatient,
    h2("文書の種類"),
    select(),
    div(
      h2("スキャナ選択"),
      button("更新")
    ),
    eScanned
  ) 
  {
    for i <- 1 to 10 do addSearchResult(mockPatient)
    eSearchResult.ele(displayDefault)
  }

  def mockPatient: Patient = Patient(
    1234,
    "田中",
    "孝志",
    "たなか",
    "たかし",
    Sex.Male,
    LocalDate.of(2002, 3, 12),
    "東京",
    "03"
  )

  def addSearchResult(patient: Patient): Unit =
    eSearchResult.add(
      String.format("(%04d) %s", patient.patientId, patient.fullName()),
      patient
    )
