package dev.myclinic.scala.web.appoint.sheet

import dev.fujiwara.domq.ElementQ.{given, *}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.Modal
import dev.myclinic.scala.model.{AppointTime, Appoint}
import dev.myclinic.scala.util.KanjiDate
import dev.fujiwara.domq.Binding.InputBinding
import dev.fujiwara.domq.Binding.TextBinding
import dev.fujiwara.domq.Binding.bindTo
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement
import org.scalajs.dom.raw.HTMLInputElement
import dev.myclinic.scala.web.appoint.Misc
import dev.myclinic.scala.webclient.Api

object MakeAppointDialog:
  def open(appointTime: AppointTime): Unit =
    val ui = new UI(appointTime)
    val dlog = Modal(
      "診察予約入力",
      close => {
        ui.setup(close)
        ui.ele
      }
    )
    dlog.open()
    ui.nameInput.focus()

  class UI(appointTime: AppointTime):
    val nameInput: HTMLInputElement = inputText()
    private val enterButton = button("入力")
    private val cancelButton = button("キャンセル")
    private val errorBox: HTMLElement = div()
    val ele = div(
      div(Modal.modalBody)(
        div(dateTimeRep(appointTime)),
        errorBox(
          display := "none",
          color := "red",
          border := "1px solid red"
        ),
        div(
          span("患者名："),
          nameInput(ml := "0.5rem")
        )
      ),
      div(Modal.modalCommands)(
        enterButton,
        cancelButton
      )
    )

    def setup(close: () => Unit): Unit =
      cancelButton(onclick := close)
      enterButton(onclick := (() => {
        validate() match {
          case Right(name) => { 
            val app = Appoint(0, appointTime.appointTimeId, name, 0, "")
            Api.registerAppoint(app)
            close() 
          }
          case Left(msg) => showError(msg)
        }
      }))

    def showError(msg: String): Unit =
      errorBox.clear()
      errorBox(msg, display := "block")

    def validate(): Either[String, String] =
      val name = nameInput.value
      if name.isEmpty then Left("患者名が入力されていません。") 
      else Right(name)

    def dateTimeRep(appointTime: AppointTime): String =
      Misc.formatAppointTimeSpan(appointTime)
