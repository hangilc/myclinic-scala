package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu}
import dev.fujiwara.domq.PullDown.pullDownLink
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers}
import dev.myclinic.scala.model.{Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.{HTMLElement, MouseEvent}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future
import dev.myclinic.scala.web.reception.cashier.Cashier
import dev.myclinic.scala.web.reception.patient.PatientManagement
import dev.myclinic.scala.web.reception.records.Records
import dev.myclinic.scala.web.reception.scan.Scan
import dev.myclinic.scala.web.appbase.HotlineUI
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.model.Hotline
import dev.myclinic.scala.web.appbase.HotlineBlock

class MainUI(using fetcher: EventFetcher):
  def invoke(label: String): Unit =
    sideMenu.invokeByLabel(label)
  val hotline = new HotlineBlock("reception", "practice")

  private var lastHotlineAppEventId = 0
  private val eMain: HTMLElement = div()
  private val sideMenu = SideMenu(
    eMain,
    List(
      "メイン" -> (() => Cashier()),
      "患者管理" -> (() => new PatientManagement()),
      "診療記録" -> (() => Records()),
      "スキャン" -> (() => Scan())
    )
  )

  val ele =
    div(id := "content")(
      div(id := "banner")("受付"),
      div(id := "workarea")(
        div(id := "side-bar")(
          sideMenu.ele(id := "side-menu"),
          hotline.ele
        ),
        eMain(id := "main")
      )
    )

  // def hotlineUI: HotlineUI =
  //   new HotlineUI:
  //     def messageInput = hotlineInput
  //     def sendButton = hotlineSendButton
  //     def rogerButton = hotlineRogerButton
  //     def beepButton = hotlineBeepButton


  // def appendHotline(appEventId: Int, hotline: Hotline): Unit =
  //   val id = appEventId
  //   if id > lastHotlineAppEventId then
  //     val rep = Setting.hotlineNameRep(hotline.sender)
  //     val msg = hotline.message
  //     hotlineMessages.value += s"${rep}> ${msg}\n"
  //     lastHotlineAppEventId = id

  // private def downTriangle(): HTMLElement =
  //   Icons.downTriangleFlat(
  //     Icons.defaultStaticStyle,
  //     ml := "0.2rem"
  //   )

  // private def insertIntoHotlineInput(s: String): Unit =
  //   val start = hotlineInput.selectionStart
  //   val end = hotlineInput.selectionEnd
  //   val left = hotlineInput.value.substring(0, start)
  //   val right = hotlineInput.value.substring(end)
  //   val index = s.indexOf("{}")
  //   val msgLeft = if index < 0 then s else s.substring(0, index)
  //   val msgRight = if index < 0 then "" else s.substring(index + 2)
  //   hotlineInput.value = left + msgLeft + msgRight + right
  //   hotlineInput.focus()
  //   val pos = start + msgLeft.size
  //   hotlineInput.selectionStart = pos
  //   hotlineInput.selectionEnd = pos

  // private def regularMenuItems: List[(String, () => Unit)] =
  //   val items: List[String] = Setting.regularHotlineMessages
  //   items.map(msg => msg -> (() => insertIntoHotlineInput(msg)))

  // private def patientMenuItems: Future[List[(String, () => Unit)]] =
  //   def exec(patient: Patient): Unit =
  //     val txt = s"""(${patient.patientId}) ${patient.fullName("")}様、"""
  //     insertIntoHotlineInput(txt)
  //   for
  //     wqueue <- Api.listWqueue()
  //     visitIds = wqueue.map(_.visitId)
  //     visitMap <- Api.batchGetVisit(visitIds)
  //     patientMap <- Api.batchGetPatient(
  //       visitMap.values.map(_.patientId).toList
  //     )
  //   yield {
  //     val patients = patientMap.values.toList
  //     patients.map(patient => {
  //       patient.fullName("") -> (() => exec(patient))
  //     })
  //   }

