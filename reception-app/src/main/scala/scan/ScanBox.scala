package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.Selection
import dev.myclinic.scala.model.Patient

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
    eSearchResult(cls := "search-result", displayNone),
    eSelectedPatient,
    h2("文書の種類"),
    select(),
    div(
      h2("スキャナ選択"),
      button("更新")
    ),
    eScanned
  )

