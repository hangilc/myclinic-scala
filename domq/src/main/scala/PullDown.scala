package dev.fujiwara.domq

import org.scalajs.dom.{HTMLElement}
import org.scalajs.dom.{document}
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.{Geometry, Icons, FloatingElement, Screen}
import scala.language.implicitConversions
import org.scalajs.dom.MouseEvent
import org.scalajs.dom.window
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global

import scala.util.Failure
import scala.{util => ju}
import scala.util.Success
import org.scalajs.dom.HTMLAnchorElement

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
    menu.insert()
    locate(menu)
    menu.show()

  def close(): Unit =
    menu.remove()
    screen.remove()
    ZIndexManager.release(zIndexMenu)
    ZIndexManager.release(zIndexScreen)
    onClose()

  def onClose(): Unit = ()

  def createContent(items: List[(String, () => Unit)]): HTMLElement =
    div(
      items.map { (t, c) =>
        {
          a(
            t,
            onclick := { () =>
              close()
              c()
            }
          )
        }
      }
    )

class PullDown(
    val anchor: HTMLElement,
    mkMenu: () => Future[List[(String, () => Unit)]]
):
  anchor(onclick := (onClick _))

  def onClick(): Unit =
    val pm = new PullDownMenu()
    for items <- mkMenu()
    yield
      val contents: List[HTMLElement] = items.map { (t, c) =>
        {
          a(
            t,
            onclick := { () =>
              pm.close()
              c()
            }
          )
        }
      }
      pm.open(div(contents), fe => PullDown.locate(anchor, fe))

object PullDown:
  def locate(anchor: HTMLElement, fe: FloatingElement): Unit =
    val r = Geometry.rectInViewport(anchor)
    fe.ele.style.left = s"${r.left + Geometry.windowScrollX}px"
    fe.ele.style.top = s"${r.top + r.height + 4}px"
    if fe.isWindowLeftOverflow then
      fe.ele.style.right = ""
      fe.ele.style.left = "4px"
    else if fe.isWindowRightOverflow then
      fe.ele.style.left = ""
      fe.ele.style.right = "4px"
    if fe.isTopOverflow: then
      ???

  def pullDownLink(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    PullDown(a(label), () => Future.successful((commands))).anchor

  def pullDownLink(
      label: String,
      commands: () => Future[List[(String, () => Unit)]]
  ): HTMLElement =
    PullDown(a(label), commands).anchor

  def pullDownButton(
      label: String,
      commands: List[(String, () => Unit)]
  ): HTMLElement =
    PullDown(button(label), () => Future.successful(commands)).anchor

  def attachPullDown(
      e: HTMLElement,
      commands: List[(String, () => Unit)]
  ): Unit =
    PullDown(e, () => Future.successful(commands))

  def attachPullDown(
      e: HTMLElement,
      commands: () => Future[List[(String, () => Unit)]]
  ): Unit =
    PullDown(e, commands)

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
