package dev.myclinic.scala.web

import org.scalajs.dom.{document, window, html}
import scala.concurrent.Future

trait Dialog[R] {
  var title: String
  def open(): Unit
  def cancel(): Unit
  def close(result: R): Unit
  val content: html.Div
  val commandBox: html.Div
  var onComplete: R => Unit
  var onCancel: () => Unit
}

// object Dialog {
//   val backDrop = div(cls := "modal-dialog-backdrop").render
  
//   def create[R](title: String): Dialog[R] = {
//     new DialogTmpl[R](){
//       override def open(): Unit = {
//         document.body.appendChild(backDrop)
//         document.body.appendChild(ele)
//       }

//       override def cancel(): Unit = {
//         document.body.removeChild(ele)
//         document.body.removeChild(backDrop)
//         onCancel()
//       }

//       override def close(result: R): Unit = {
//         dialogValue = result
//         document.body.removeChild(ele)
//         onComplete(result)
//       }

//       var onComplete: R => Unit = _ => ()

//       var onCancel: () => Unit = () => ()
//     }
//   }
// }

// private abstract class DialogTmpl[R] extends Dialog[R]{
//   val titleSpan = span("TITLE").render

//   val closeAnchor = a(
//     href := "javascript:void(0)",
//     style := "font-size: 1.2rem",
//     cls := "align-item-center ml-2",
//     onclick := { () => cancel() }
//   )(raw("&times;"))

//   val content: html.Div = div().render

//   val commandBox: html.Div = div(cls := "command-box").render

//   val ele = div(cls := "modal-dialog-content", maxHeight := calcMaxHeight())(
//     div(cls := "d-flex title-wrapper justify-content-between")(
//       h3(cls := "d-inline-block")(titleSpan),
//       closeAnchor
//     ),
//     content,
//     commandBox,
//   ).render

//   var dialogValue: R = _

//   def calcMaxHeight(): String = {
//     val h = window.innerHeight - 40
//     s"${h}px"
//   }

//   def title: String = titleSpan.innerText

//   def title_=(text: String): Unit = titleSpan.innerText = text

// }

// object Dialog {
//   val titleSpan = span("TITLE").render
//   val dom = div(cls := "modal-dialog-content", maxHeight := calcMaxHeight())(
//     div(cls := "d-flex title-wrapper justify-content-between")(
//       h3(cls := "d-inline-block")(titleSpan),
//       a(href := "javascript:void(0)", style := "font-size: 1.2rem",
//         cls := "align-item-center")(raw("&times;"))
//     ),
//     div(),
//     div(cls := "command-box")
//   ).render
//   val backDrop = div(cls := "modal-dialog-backdrop").render

//   def calcMaxHeight(): String = {
//     val h = window.innerHeight - 40
//     s"${h}px"
//   }

//   def close[A]: Future[Option[A]] = {

//   }

//   def open[A](title: String): Future[Option[A]] = {
//     titleSpan.innerText = title
//     document.body.appendChild(backDrop)
//     document.body.appendChild(dom)
//     Future.successful(None)
//   }
