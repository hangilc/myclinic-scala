package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.ElementQ._

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

  def closeButton: Modifier = attr("data-bs-dismiss") := "modal"
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
        div(cls := "modal-footer", cb := (eCommands = _))
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

  val modal = new Bootstrap.Modal(ele)

  ele.addEventListener("hidden.bs.modal", (_: Event) => {
    println("closed")
    modal.dispose()
    document.body.removeChild(ele)
    onClosedHandler(result)
  })

  override def open(): Unit = {
    modal.show()
    document.body(style := "hidden: auto")
  }

  override def close(): Unit = {
    modal.hide()
  }

  private var onClosedHandler: Option[R] => Unit = _ => ()

  def onClosed(handler: Option[R] => Unit): Unit = onClosedHandler = handler
}
