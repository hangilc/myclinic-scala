package dev.myclinic.scala.web

import org.scalajs.dom.{document, window, html}
import scala.concurrent.Future
import dev.myclinic.scala.web.{DomUtil, Tmpl}
import org.scalajs.dom.raw.Element

trait Dialog[R] {
  var title: String
  def open(): Unit
  def cancel(): Unit
  def close(result: R): Unit
  val content: Element
  val commandBox: Element
  var onComplete: R => Unit
  var onCancel: () => Unit
}

private object DialogTmpl {
  val tmpl = """
   <div class="modal" tabindex="-1" role="dialog" data-backdrop="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title x-title"></h5>
                    <button type="button" class="close x-close" data-dismiss="modal" 
                      aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body x-content"> </div>
                <div class="modal-footer x-commaand-box"> </div>
            </div>
        </div>
    </div>
  """
  val backDrop = Tmpl.createElement("""
    <div class="modal-dialog-backdrop"></div>
  """)

}

private class DialogImpl[R]() extends Dialog[R] {
  val ele = Tmpl.createElement(DialogTmpl.tmpl)
  var eTitle: Element = _
  var eCloseLink: Element = _
  var eContent: Element = _
  var eCommandBox: Element = _
  DomUtil.traversex(ele, (name, e) => {
    name match {
      case "title" => eTitle = e
      case "close" => eCloseLink = e
      case "content" => eContent = e
      case "command-box" => eCommandBox = e
      case _ =>
    }
  })

  def title: String = eTitle.innerText

  def title_=(v: String): Unit = eTitle.innerText = v;

  def open(): Unit = {
    document.body.appendChild(DialogTmpl.backDrop)
    document.body.appendChild(ele)
  }

  def cancel(): Unit = {
    document.body.removeChild(ele)
    document.body.removeChild(DialogTmpl.backDrop)
    onCancel()
  }

  def close(result: R): Unit = {
    document.body.removeChild(ele)
    document.body.removeChild(DialogTmpl.backDrop)
    onComplete(result)
  }

  val content: Element = eContent

  val commandBox: Element = eCommandBox

  var onComplete: R => Unit = _ => ()

  var onCancel: () => Unit = () => ()
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
