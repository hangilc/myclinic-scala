package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.raw.Element
import org.scalajs.dom.raw.Event
import dev.fujiwara.domq.Modifiers._
import dev.fujiwara.domq.Html._
import dev.fujiwara.domq.ElementQ._
import dev.fujiwara.domq.Binding._

// trait Dialog[R] {
//   def open(): Unit
//   def content: Element
//   def commands: Element
//   var result: Option[R]
//   def close(): Unit
//   def onClosed(handler: Option[R] => Unit): Unit
// }

class Dialog[R](handler: R => Unit) {
  var result: Option[R] = None
  val titleBinding = Binding.TextBinding()

  val ele = div(cls := "modal", attr("tabindex") := "-1")(
    div(cls := "modal-dialog")(
      div(cls := "modal-content")(
        div(cls := "modal-header")(
          h5(cls := "modal-title", bindTo(titleBinding)),
          button(attr("type") := "button", cls := "btn-close", Dialog.closeButton)
        ),
        div(cls := "modal-body", cb := (setupContent _))),
        div(cls := "modal-footer", cb := (setupCommandBox _))
      )
    )
  
  val modal = new Bootstrap.Modal(ele)

  titleBinding.text = "Untitled"

  def setupContent(content: Element): Unit = ()
  def setupCommandBox(commandBOx: Element): Unit = ()

  ele.addEventListener("hidden.bs.modal", (_: Event) => {
    modal.dispose()
    document.body.removeChild(ele)
    if( result.isDefined ){
      handler(result.get)
    }
  })

def open(): Unit = {
    modal.show()
    document.body(style := "hidden: auto")
  }

  def close(): Unit = {
    modal.hide()
  }
}

object Dialog {

  def closeButton: Modifier = attr("data-bs-dismiss") := "modal"
  
}


// object Dialog {
//   def apply[R](title: String): Dialog[R] = {
//     val dlog = new DialogImpl[R](title)
//     dlog
//   }

//   def closeButton: Modifier = attr("data-bs-dismiss") := "modal"
// }

// private class DialogImpl[R](title: String) extends Dialog[R] {
//   var result: Option[R] = None
//   var eContent, eCommands: Element = null
//   )

//   def content: Element = {
//     require(eContent != null)
//     eContent
//   }

//   def commands: Element = {
//     require(eCommands != null)
//     eCommands
//   }

//   val modal = new Bootstrap.Modal(ele)

//   ele.addEventListener("hidden.bs.modal", (_: Event) => {
//     modal.dispose()
//     document.body.removeChild(ele)
//     onClosedHandler(result)
//   })

//   override def open(): Unit = {
//     modal.show()
//     document.body(style := "hidden: auto")
//   }

//   override def close(): Unit = {
//     modal.hide()
//   }

//   private var onClosedHandler: Option[R] => Unit = _ => ()

//   def onClosed(handler: Option[R] => Unit): Unit = onClosedHandler = handler
// }
