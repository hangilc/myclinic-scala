package dev.myclinic.scala.web

import org.scalajs.dom.{document, window, html}
import scala.concurrent.Future
import dev.myclinic.scala.web.{DomUtil, Tmpl}
import dev.myclinic.scala.web.Implicits._
import dev.myclinic.scala.web.html._
import dev.myclinic.scala.web.Modifiers._
import org.scalajs.dom.raw.{Element, MouseEvent}

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
    <div class="modal-dialog-content">
        <div class="d-flex title-wrapper justify-content-between">
            <h3 class="d-inline-block x-title">タイトル</h3>
            <a href="javascript:void(0)" style="font-size: 1.2rem" 
              class="align-item-center x-close-link">&times;</a>
        </div>
        <div class="x-content" style="min-width:160px"></div>
        <div class="command-box x-footer"></div>
    </div>
  """

  val backDrop = div(cls := "modal-dialog-backdrop")

  //   Tmpl.createElement("""
  //   <div class="modal-dialog-backdrop"></div>
  // """)

}

object Dialog {
  def create[R](title: String): Dialog[R] = {
    val dlog = new DialogImpl[R]()
    dlog.title = title
    dlog
  }

}

private class DialogImpl[R]() extends Dialog[R] {
  var eTitle: Element = _
  var eCloseLink: Element = _
  var eContent: Element = _
  var eCommandBox: Element = _

  //val ele: Element = Tmpl.createElement(DialogTmpl.tmpl)
  val ele: Element = div(cls := "modal-dialog-content")(
    div(cls := "d-flex title-wrapper justify-content-between")(
      h3(cls := "d-inline-block", cb := (eTitle = _)),
      a(
        href := "",
        style := "font-size: 1.2rem",
        cls := "align-item-center",
        cb := (eCloseLink = _)
      )(raw("&times;"))
    ),
    div(style := "min-width:160px", cb := (eContent = _)),
    div(cls := "command-box", cb := (eCommandBox = _))
  )
  DomUtil.traversex(
    ele,
    (name, e) => {
      name match {
        case "title"       => eTitle = e
        case "close-link"  => eCloseLink = e
        case "content"     => eContent = e
        case "command-box" => eCommandBox = e
        case _             =>
      }
    }
  )
  eCloseLink.onclick(cancel _)

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
