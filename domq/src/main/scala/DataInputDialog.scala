package dev.fujiwara.domq

import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}

case class DataInputDialog[T](
    title: String,
    initText: String,
    fromText: String => Either[String, T],
    onEnter: T => Unit,
):
    val input = Html.input
    val errBox = ErrorBox()
    val dlog = ModalDialog3()
    dlog.title(title)
    dlog.body(
        errBox.ele,
        input(cls := "domq-data-input-dialog-input", value := initText)
    )
    dlog.commands(
        button("入力", onclick := (doEnter _)),
        button("キャンセル", onclick := (() => dlog.close()))
    )

    def doEnter(): Unit =
        fromText(input.value) match {
            case Left(msg) => errBox.show(msg)
            case Right(t) => 
                dlog.close()
                onEnter(t)
        }

