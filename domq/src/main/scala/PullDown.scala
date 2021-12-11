package dev.fujiwara.domq

import org.scalajs.dom.raw.{HTMLElement}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Geometry, Icons, FloatingElement, Screen}
import scala.language.implicitConversions

object PullDown:
  def pullDownLink(label: String, content: HTMLElement): HTMLElement =
    val link = a(cls := "domq-pull-down link")(
      label,
      Icons.downTriangleFlat
    )
    bind(link, content)

  private def bind(anchor: HTMLElement, content: HTMLElement): HTMLElement =
    lazy val menu = makeMenu(anchor, content)
    anchor(onclick := (() => menu.toggle()))

  private def makeMenu(
      anchor: HTMLElement,
      content: HTMLElement
  ): FloatingElement =
    val f = FloatingElement(content)
    val aRect = Geometry.getRect(anchor)
    val p = aRect.leftBottom.shiftY(4)
    f.leftTop = p
    f

  private def makeScreen(): HTMLElement =
    val zIndex = ZIndexManager.alloc()
    Screen.create(zIndex)

// val btn: HTMLElement = button(cls := "domq-pull-down-button")(
//   label,
//   Icons.downTriangleFlat()(cls := "domq-pull-down-arrow")
// )
