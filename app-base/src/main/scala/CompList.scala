package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}

object CompList:
  def append[C](c: C, list: List[C], wrapper: HTMLElement)(using
      comp: Comp[C]
  ): List[C] =
    wrapper(comp.ele(c))
    list :+ c

  def prepend[C](c: C, list: List[C], wrapper: HTMLElement)(using
      comp: Comp[C]
  ): List[C] =
    wrapper.prepend(comp.ele(c))
    c :: list

  def clear[C](list: List[C])(using comp: Comp[C]): List[C] =
    list.foreach(c => comp.ele(c).remove())
    List.empty
