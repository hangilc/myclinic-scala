package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

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

  enum AskCommand:
    case Ok, Cancel

  def ask(
      title: String,
      message: String,
      commands: Set[AskCommand],
      cb: AskCommand => Unit,
      defaultValue: Option[AskCommand]
  ): Unit =
    Modal(title, close => {
      div(
        div(Modal.modalBody)(

        ),
        div(Modal.modalCommands){

        }
      )
    }, cb, defaultValue)
