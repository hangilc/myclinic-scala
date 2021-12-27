package dev.myclinic.scala.web.reception.scan

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class Scan extends SideMenuService:
  val newScanButton: HTMLElement = button("新規スキャン")
  val eScannedBoxes: HTMLElement = div()
  def getElement: HTMLElement =
    div(cls := "scan")(
      div(cls := "header")(
        h1("スキャン"),
        newScanButton
      ),
      eScannedBoxes
    )
  addBox()

  def addBox(): Future[Unit] =
    val box = new ScanBox((onBoxClose _))
    for
      _ <- box.init()
    yield
      eScannedBoxes.prepend(box.ele)

  private def countBoxes: Int =
    eScannedBoxes.qSelectorAll(s".${ScanBox.cssClassName}").size

  private def onBoxClose(): Unit =
    if countBoxes == 0 then addBox()
    

