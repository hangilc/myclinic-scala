package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.Element

object Viewport:
  private def root: Element = document.documentElement

  def width: Int = root.clientWidth
  def height: Int = root.clientHeight
  def offsetLeft: Double = root.scrollLeft
  def offsetTop: Double = root.scrollTop