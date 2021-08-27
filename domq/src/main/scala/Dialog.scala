package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Binding._

class Dialog[R]() {
  val titleBinding = Binding.TextBinding()
  var contentBinding = Binding.ElementBinding()
  var commandBoxBinding = Binding.ElementBinding()

  val ele = div(cls := "modal", attr("tabindex") := "-1")(
    div(cls := "modal-dialog")(
      div(cls := "modal-content")(
        div(cls := "modal-header")(
          h5(cls := "modal-title", bindTo(titleBinding)),
          button(
            attr("type") := "button",
            cls := "btn-close",
            Dialog.closeButton
          )
        ),
        div(cls := "modal-body", bindTo(contentBinding)),
        div(cls := "modal-footer", bindTo(commandBoxBinding))
      )
    )
  )

  val modal = new Bootstrap.Modal(ele)

  titleBinding.text = "Untitled"

  def content: Element = contentBinding.element
  def commandBox: Element = commandBoxBinding.element

  ele.addEventListener(
    "hidden.bs.modal",
    (_: Event) => {
      modal.dispose()
      document.body.removeChild(ele)
    }
  )

  def open(): Unit = {
    modal.show()
    document.body(style := "hidden: auto")
  }

  def close(): Unit = {
    modal.hide()
  }

  def title: String = titleBinding.text

  def title_=(v: String): Unit = titleBinding.text = v
}

object Dialog {

  def closeButton: Modifier = attr("data-bs-dismiss") := "modal"

}