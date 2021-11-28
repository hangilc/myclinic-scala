package dev.myclinic.scala.web.reception.patient

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.raw.MouseEvent
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global

class PatientManagement() extends SideMenuService:
  val eSearchText: HTMLInputElement = inputText()
  val eWorkarea: HTMLElement = div()
  val ele: HTMLElement = div(cls := "patient-management")(
    div(
      h1("受付管理", display := "inline-block"),
      Icons.menu(size = "1.2rem", color = "gray")(
        cssFloat := "right",
        onclick := (onMenu _),
        ml := "0.5rem",
        Icons.defaultStyle
      ),
      form(cls := "search-patient-box", onsubmit := (onSearch _))(
        button("新規患者", onclick := (onNewPatient _)),
        eSearchText(),
        button("検索", attr("type") := "submit")
      )
    ),
    eWorkarea
  )
  def getElement: HTMLElement = ele
  override def init(): Future[Unit] = Future.successful(())
  override def onReactivate: Future[Unit] = Future.successful(())
  override def dispose(): Unit =
    subscribers.foreach(_.unsubscribe())

  private val subscribers: List[EventSubscriber[_]] = List.empty

  private def onMenu(event: MouseEvent): Unit =
    ()

  private var newPatientBlock: Option[NewPatientBlock] = None

  private def onNewPatient(): Unit =
    val block: NewPatientBlock = newPatientBlock.getOrElse({
      val b = NewPatientBlock(bb => {
        bb.ele.remove()
        newPatientBlock = None
      })
      newPatientBlock = Some(b)
      b
    })
    eWorkarea.prepend(block.ele)

  private def onSearch(): Unit =
    val txt = eSearchText.value.trim
    if !txt.isEmpty then
      for
        patients <- Api.searchPatient(txt)
      yield {
        println(("patients", patients))
      }

  
