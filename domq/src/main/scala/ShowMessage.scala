package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.raw.HTMLElement

object ShowMessage:
  def showMessage(msg: String, title: String = "メッセージ"): Unit =
    val b = button("OK")
    val m: Modal = Modal(
      title,
      div(msg),
      div(b)
    )
    b(onclick := (() => m.close()))
    m.open()

  def showError(msg: String, zIndex: Int = 2002): Unit =
    val b = button("OK")
    val m: Modal = Modal(
      "エラー",
      div(color := "red")(msg),
      div(b)
    )
    b(onclick := (() => m.close()))
    m.open()


  enum AskCommand(val label: String):
    case Ok extends AskCommand("OK")
    case Enter extends AskCommand("入力")
    case Cancel extends AskCommand("キャンセル")

  private def populateCommandBox(
      box: HTMLElement,
      commands: List[AskCommand],
      cb: AskCommand => Unit
  ): Unit =
    val items: List[Modifier] =
      commands.map(cmd => button(cmd.label)(onclick := (() => cb(cmd))))
    box(items: _*)

  def choice(
      title: String,
      message: String,
      commands: List[AskCommand],
      defaultValue: AskCommand,
      cb: AskCommand => Unit
  ): Unit =
    val commandBox = div()
    val m = Modal(
      title,
      div(message),
      commandBox
    )
    populateCommandBox(commandBox, commands, cmd => m.close())
    m.open()

  def confirm(message: String, cb: Boolean => Unit): Unit =
    val yesButton = button("はい")
    val noButton = button("いいえ")
    val m = Modal(
      "確認",
      div(innerText := message),
      div(yesButton, noButton)
    )
    m.dialog(css(style => {
      style.maxWidth = "400px"
    }))
    m.onClose(cb)
    yesButton(onclick := (() => m.close(true)))
    noButton(onclick := (() => m.close(false)))
    m.open()

  def getString(
      title: String,
      message: String,
      cb: Option[String] => Unit
  ): Unit =
    import AskCommand.*
    val commandBox = div()
    val inputElement = input(attr("type") := "text")
    val m = Modal(
      title,
      div(
        div(message),
        div(inputElement)
      ),
      commandBox
    )
    populateCommandBox(
      commandBox,
      List(Enter, Cancel),
      {
        case Enter => cb(Some(inputElement.value))
        case _     => cb(None)
      }
    )
    m.open()
