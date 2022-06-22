package dev.fujiwara.domq

import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions

case class DataInputDialog[T](
    title: String,
    initText: String,
    fromText: String => Either[String, T]
):
  val onEnterPublisher = new LocalEventPublisher[T]
  val onCancelPublisher = new LocalEventPublisher[Unit]
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

  def onEnter(handler: T => Unit): Unit =
    onEnterPublisher.subscribe(handler)

  def onCancel(handler: () => Unit): Unit =
    onCancelPublisher.subscribe(_ => handler())

  def open(): Unit = dlog.open()

  private def doEnter(): Unit =
    fromText(input.value) match {
      case Left(msg) => errBox.show(msg)
      case Right(t) => 
        dlog.close()
        onEnterPublisher.publish(t)
    }

  private def doCancel(): Unit =
    dlog.close()
    onCancelPublisher.publish(())
