package dev.fujiwara.domq

import org.scalajs.dom.{document, window}
import org.scalajs.dom.Element
import org.scalajs.dom.HTMLElement

object Viewport:
  private def root: Element = document.documentElement

  def width: Int = root.clientWidth
  def height: Int = root.clientHeight
  def offsetLeft: Int = root.scrollLeft.toInt
  def offsetTop: Int = root.scrollTop.toInt

