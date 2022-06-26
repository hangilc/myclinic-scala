package dev.myclinic.scala.web.reception.patient

import dev.myclinic.scala.web.appbase.{SideMenuService, SyncedDataSource}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, ShowMessage}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.concurrent.Future
import org.scalajs.dom.MouseEvent
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.util.{Success, Failure}

import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.{EventPublishers, EventFetcher}

class PatientManagement(using EventFetcher) extends SideMenuService:
  val eSearchText: HTMLInputElement = inputText()
  val eWorkarea: HTMLElement = div()
  val ele: HTMLElement = div(cls := "patient-management")(
    div(
      h1("患者管理", display := "inline-block"),
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

  override def getElement: HTMLElement = ele(cls := "content")

  def initFocus(): Unit =
    eSearchText.focus()
  override def init(): Future[Unit] =
    initFocus()
    Future.successful(())
  override def onReactivate: Future[Unit] =
    initFocus()
    Future.successful(())
  override def dispose(): Unit =
    ()

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
      for (gen, patients) <- Api.searchPatient(txt)
      yield {
        if patients.size > 0 then
          val block = SearchPatientBlock(
            patients,
            _.remove(),
            patient => addManagePatientBlock(gen, patient)
          )
          eWorkarea.prepend(block.ele)
          eSearchText.value = ""
        else ShowMessage.showMessage("該当患者が見つかりませんでした。")
      }

  private def addManagePatientBlock(gen: Int, patient: Patient): Unit =
    val ds: SyncedDataSource[Patient] = SyncedDataSource(gen, patient)
    eWorkarea.prepend(ManagePatientBlock(ds).ele)

