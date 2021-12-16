package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}

class Scan extends SideMenuService:
  val newScanButton: HTMLElement = button("新規スキャン")
  val scannedBoxes: HTMLElement = div()
  def getElement: HTMLElement =
    div(cls := "scan")(
      div(cls := "header")(
        h1("スキャン"),
        newScanButton
      ),
      scannedBoxes
    )
  addBox()

  def addBox(): Unit =
    val box = new ScanBox()
    scannedBoxes.prepend(box.ele)

