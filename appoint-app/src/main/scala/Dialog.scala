package dev.myclinic.scala.web

import dev.myclinic.scala.web.Bs
import dev.myclinic.scala.web.Implicits._
import dev.myclinic.scala.web.Modifiers._
import dev.myclinic.scala.web.html._
import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event

trait Dialog[R] {
  def open(): Unit
  def content: Element
  def commands: Element
  var result: Option[R]
  def close(): Unit
  def onClosed(handler: Option[R] => Unit): Unit
}

object Dialog {
  def apply[R](title: String): Dialog[R] = {
    val dlog = new DialogImpl[R](title)
    dlog
  }

  def closeButton: ElementModifier = attr("data-bs-dismiss") := "modal"
}

private class DialogImpl[R](title: String) extends Dialog[R] {
  var result: Option[R] = None
  var eContent, eCommands: Element = null
  val ele = div(cls := "modal", attr("tabindex") := "-1")(
    div(cls := "modal-dialog")(
      div(cls := "modal-content")(
        div(cls := "modal-header")(
          h5(cls := "modal-title")(title),
          button(attr("type") := "button", cls := "btn-close", Dialog.closeButton)
        ),
        div(cls := "modal-body", cb := (eContent = _)),
        div(cls := "modal-footer", cb := (eCommands = _))(
          // button(attr("type") := "button", cls := "btn btn-secondary",
          //   Dialog.closeButton)("Close"),
          // button(attr("type") := "button", cls := "btn btn-primary")("Save changes")
        )
      )
    )
  )

  def content: Element = {
    require(eContent != null)
    eContent
  }

  def commands: Element = {
    require(eCommands != null)
    eCommands
  }

  val modal = new Bs.Modal(ele)

  ele.addEventListener("hidden.bs.modal", (_: Event) => {
    println("closed")
    modal.dispose()
    document.body.removeChild(ele)
    onCloseHandler(result)
  })

  override def open(): Unit = {
    modal.show()
    document.body(style := "hidden: auto")
  }

  override def close(): Unit = {
    modal.hide()
  }

  private var onCloseHandler: Option[R] => Unit = _ => ()

  def onClosed(handler: Option[R] => Unit): Unit = onCloseHandler = handler
}
