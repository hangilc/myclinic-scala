package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement}
import org.scalajs.dom.{document}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Geometry, Icons, FloatingElement, Screen}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.Failure
import scala.{util => ju}
import scala.util.Success

class PullDownMenu:
  val screen: HTMLElement = Screen.screen
  val wrapper: HTMLElement = div()
  val menu: FloatingElement = FloatingElement(wrapper)
  val zIndexScreen = ZIndexManager.alloc()
  val zIndexMenu = ZIndexManager.alloc()

  def open(content: HTMLElement, locate: FloatingElement => Unit): Unit =
    wrapper(content)
    screen(zIndex := zIndexScreen)(
      onclick := ((e: MouseEvent) => {
        e.preventDefault()
        e.stopPropagation()
        close()
      })
    )
    menu.ele(zIndex := zIndexMenu)
    document.body(screen)
    locate(menu)
    menu.show()

  def close(): Unit =
    menu.hide()
    screen.remove()
    ZIndexManager.release(zIndexMenu)
    ZIndexManager.release(zIndexScreen)

object PullDown:
  def locatePullDownMenu(anchor: HTMLElement, f: FloatingElement): Unit =
    val rect = Geometry.getRect(anchor)
    val p = rect.leftBottom.shiftY(4)
    f.leftTop = p

  def pullDown(
      anchor: HTMLElement,
      content: ((() => Unit) => HTMLElement)
  ): HTMLElement =
    anchor(
      onclick := (() => {
        val m = new PullDownMenu()
        val c = content(() => m.close())
        m.open(c, f => locatePullDownMenu(anchor, f))
      })
    )

  def pullDownFuture(
      anchor: HTMLElement,
      content: (() => Unit) => Future[HTMLElement]
  ): HTMLElement =
    anchor(
      onclick := (() => {
        val m = new PullDownMenu()
        (for
          c <- content(() => m.close())
        yield {
          m.open(c, f => locatePullDownMenu(anchor, f))
        }).onComplete {
          case Success(_) => ()
          case Failure(ex) => System.err.println(ex.getMessage)
        }
      })
    )

  def pullDownLink(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    pullDown(createLinkAnchor(label), close => createContent(close, commands))

  def pullDownLink(
      label: String,
      commands: Future[List[(String, () => Unit)]]
  ): HTMLElement =
    pullDownFuture(createLinkAnchor(label), close => {
      for
        c <- commands
      yield createContent(close, c)
    })

  def pullDownButton(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    pullDown(createButtonAnchor(label), close => createContent(close, commands))

  def createLinkAnchor(label: String): HTMLElement =
    a(cls := "domq-pull-down link")(
      label,
      Icons.downTriangleFlat
    )

  def createButtonAnchor(label: String): HTMLElement =
    button(cls := "domq-pull-down button")(
      label,
      Icons.downTriangleFlat
    )

  def createContent(
      close: () => Unit,
      items: List[(String, () => Unit)]
  ): HTMLElement =
    def anchor(label: String, f: () => Unit): HTMLElement =
      a(label, onclick := (() => {
        close()
        f()
      }))
    div(cls := "domq-context-menu")(children :=
      items.map({ case (label, f) =>
        div(anchor(label, f))
      })
    )
