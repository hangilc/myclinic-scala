package dev.fujiwara.domq

import org.scalajs.dom.document
import org.scalajs.dom.Element

object Viewport:
  private def root: Element = document.documentElement

  // def width: Int = root.clientWidth
  def width: Int = root.getBoundingClientRect().width.toInt
  def height: Int = root.getBoundingClientRect().height.toInt
  def offsetLeft: Int = root.scrollLeft.toInt
  def offsetTop: Int = root.scrollTop.toInt