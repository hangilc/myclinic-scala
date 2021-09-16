package dev.fujiwara.domq

import org.scalajs.dom.raw.Element

object Traverse {

  def traverse(ele: Element, cb: Element => Unit): Unit =
    cb(ele)
    val children = ele.children
    for i <- 0 until children.length do
      traverse(children.item(i), cb)

  def traversex(ele: Element, cb: (String, Element) => Unit): Unit =
    traverse(
      ele,
      e => {
        var x: Option[String] = None
        val classList = e.classList
        for i <- 0 until classList.length do {
          val cls = classList.item(i)
          if cls.startsWith("x-") then {
            x = Some(cls.substring(2))
            classList.remove(cls)
          }
        }
        x match {
          case Some(xcls) => cb(xcls, e)
          case _          =>
        }
      }
    )

}