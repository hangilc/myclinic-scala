package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement

object ShowMessage:
  def showMessage(msg: String, title: String = "メッセージ"): Unit =
    Modal(
      title,
      close => {
        div(
          div(Modal.modalBody)(
            msg
          ),
          div(Modal.modalCommands)(
            button("OK", onclick := (() => close()))
          )
        )
      }
    ).open()

  enum AskCommand(val label: String):
    case Ok extends AskCommand("Ok")
    case Enter extends AskCommand("入力")
    case Cancel extends AskCommand("キャンセル")

  private def populateCommandBox(
      box: HTMLElement,
      commands: List[AskCommand],
      cb: AskCommand => Unit
  ): Unit =
    val items: List[Modifier] = commands.map(cmd =>
      button(cmd.label)(onclick := (() => cb(cmd)))
    )
    box(items: _*)

  def choice(
      title: String,
      message: String,
      commands: List[AskCommand],
      defaultValue: AskCommand,
      cb: AskCommand => Unit
  ): Unit =
    val commandBox = div()
    val m = new Modal[AskCommand](
      title,
      close => {
        div(
          div(Modal.modalBody)(
            message
          ),
          commandBox(Modal.modalCommands)
        )
      },
      cb,
      Some(defaultValue)
    )
    populateCommandBox(commandBox, commands, cmd => m.close(Some(cmd)))
    m.open()

  def getString(
      title: String,
      message: String,
      cb: Option[String] => Unit
  ): Unit =
    import AskCommand.*
    val commandBox = div()
    val inputElement = input(attr("type") := "text")
    val m = new Modal[AskCommand](
      title,
      close => {
        div(
          div(Modal.modalBody)(
            div(message),
            div(inputElement)
          ),
          commandBox(Modal.modalCommands)
        )
      },
      {
        case Enter => cb(Some(inputElement.value))
        case _ => cb(None)
      },
      Some(Cancel)
    )
    populateCommandBox(commandBox, List(Enter, Cancel), cmd => m.close(Some(cmd)))
    m.open()
