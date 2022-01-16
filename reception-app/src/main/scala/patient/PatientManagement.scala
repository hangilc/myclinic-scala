package dev.myclinic.scala.web.reception.patient

import dev.myclinic.scala.web.appbase.SideMenuService
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import dev.myclinic.scala.web.appbase.EventSubscriber
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import dev.myclinic.scala.model.Patient

class PatientManagement() extends SideMenuService:
  val eSearchText: HTMLInputElement = inputText()
  val eWorkarea: HTMLElement = div()
  val ele: HTMLElement = div(cls := "patient-management")(
    div(
      h1("受付管理", display := "inline-block"),
      Icons.menu(
        cssFloat := "right",
        onclick := (onMenu _),
        ml := "0.5rem",
        Icons.defaultStyle
      ),
      div(cls := "search-patient-box")(
        button("新規患者", onclick := (onNewPatient _)),
        form(cls := "search-patient-form", onsubmit := (onSearch _))(
          eSearchText(),
          button("検索", attr("type") := "submit")
        )
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

  private def onNewPatient(event: MouseEvent): Unit =
    event.stopPropagation()
    event.preventDefault()
    val block: NewPatientBlock = newPatientBlock.getOrElse({
      val b = NewPatientBlock(bb => {
        bb.ele.remove()
        newPatientBlock = None
      })
      newPatientBlock = Some(b)
      b
    })
    eWorkarea.prepend(block.ele)
    block.eLastNameInput.focus()

  private def onSearch(): Unit =
    val txt = eSearchText.value.trim
    if !txt.isEmpty then
      for patients <- Api.searchPatient(txt)
      yield {
        if patients.size > 0 then
          val block = SearchPatientBlock(
            patients, 
            _.remove(),
            addManagePatientBlock
          )
          eWorkarea.prepend(block.ele)
          eSearchText.value = ""
        else ShowMessage.showMessage("該当患者が見つかりませんでした。")
      }

  private def addManagePatientBlock(patient: Patient): Unit =
    val manage = ManagePatientBlock(patient, _.ele.remove())
    eWorkarea.prepend(manage.ele)
    manage.init()
