package dev.myclinic.scala.web.reception

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{ShowMessage, Icons, Colors, ContextMenu}
import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.{SideMenu, EventPublishers}
import dev.myclinic.scala.web.reception.cashier.Cashier
import dev.myclinic.scala.model.{HotlineCreated, Patient}
import dev.myclinic.scala.webclient.Api
import org.scalajs.dom.raw.{HTMLElement, MouseEvent}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure
import scala.concurrent.Future

abstract class MainUI(using publishers: EventPublishers):
  def postHotline(msg: String): Unit
  def invoke(label: String): Unit =
    sideMenu.invokeByLabel(label)

  private var lastHotlineAppEventId = 0
  private val hotlineInput = textarea()
  private val hotlineMessages = textarea()
  private val eMain: HTMLElement = div()
  private val sideMenu = SideMenu(eMain,
    List(
      "メイン" -> (makeCashier _),
      "患者管理" -> (() => Future.successful(div("患者管理"))),
      "診療記録" -> (() => Future.successful(div("診療記録"))),
      "スキャン" -> (() => Future.successful(div("スキャン")))
    )
  )
  val ele =
    div(id := "content")(
      div(id := "banner")("受付"),
      div(id := "workarea")(
        div(id := "side-bar")(
          sideMenu.ele(id := "side-menu"),
          hotlineInput(id := "hotline-input"),
          div(id := "hotline-commands")(
            button(
              "送信",
              onclick := (() => {
                postHotline(hotlineInput.value)
                hotlineInput.value = ""
              })
            ),
            button(
              "了解",
              onclick := (() => {
                postHotline("了解")
              })
            ),
            button("Beep", onclick := (() => { Api.beep(); () })),
            a(
              span("常用", downTriangle()),
              onclick := (doRegular _)
            ),
            a("患者", downTriangle(), onclick := (doPatients _))
          ),
          hotlineMessages(
            id := "hotline-messages",
            attr("readonly") := "readonly",
            attr("tabindex") := "-1"
          )
        ),
        eMain(id := "main")
      )
    )

  def appendHotline(evt: HotlineCreated): Unit =
    val id = evt.appEventId
    if id > lastHotlineAppEventId then
      val rep = Setting.hotlineNameRep(evt.created.sender)
      val msg = evt.created.message
      hotlineMessages.value += s"${rep}> ${msg}\n"
      lastHotlineAppEventId = id

  private def downTriangle(): HTMLElement =
    Icons.downTriangleFlat(size = "0.6rem", color = Colors.primary)(
      Icons.defaultStaticStyle,
      ml := "0.2rem"
    )

  private def insertIntoHotlineInput(s: String): Unit =
    val start = hotlineInput.selectionStart
    val end = hotlineInput.selectionEnd
    val left = hotlineInput.value.substring(0, start)
    val right = hotlineInput.value.substring(end)
    val index = s.indexOf("{}")
    val msgLeft = if index < 0 then s else s.substring(0, index)
    val msgRight = if index < 0 then "" else s.substring(index + 2)
    hotlineInput.value = left + msgLeft + msgRight + right
    hotlineInput.focus()
    val pos = start + msgLeft.size
    hotlineInput.selectionStart = pos
    hotlineInput.selectionEnd = pos

  private def doRegular(event: MouseEvent): Unit =
    val items: List[String] = Setting.regularHotlineMessages
    val menu = ContextMenu(
      items.map(msg => msg -> (() => insertIntoHotlineInput(msg)))
    )
    menu.open(event)

  private def doPatients(event: MouseEvent): Unit =
    def exec(patient: Patient): Unit =
      insertIntoHotlineInput(patient.fullName(""))
    val f =
      for
        wqueue <- Api.listWqueue()
        visitIds = wqueue.map(_.visitId)
        visitMap <- Api.batchGetVisit(visitIds)
        patientMap <- Api.batchGetPatient(
          visitMap.values.map(_.patientId).toList
        )
      yield {
        val patients = patientMap.values.toList
        val menu = ContextMenu(patients.map(patient => {
          patient.fullName("") -> (() => exec(patient))
        }))
        menu.open(event)
      }
    f.onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  def makeCashier(): Future[HTMLElement] =
    val cashier = Cashier()
    for 
      _ <- cashier.refresh()
    yield cashier.ele

