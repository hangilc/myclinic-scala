package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement}
import org.scalajs.dom.document

class Screened(
    content: HTMLElement,
    locator: FloatingElement => Unit,
    onClose: () => Unit = () => ()
):
  val zIndexScreen = ZIndexManager.alloc()
  val zIndexMenu = ZIndexManager.alloc()
  val f = FloatingElement(content)
  val screen = Screen.screen
  f.ele(zIndex := zIndexMenu)
  screen(
    zIndex := zIndexScreen,
    onclick := (close _)
  )

  def open(): Unit =
    locator(f)
    document.body(screen)
    f.show()

  def close(): Unit =
    f.hide()
    screen.remove()
    ZIndexManager.release(zIndexMenu)
    ZIndexManager.release(zIndexScreen)
    onClose()


