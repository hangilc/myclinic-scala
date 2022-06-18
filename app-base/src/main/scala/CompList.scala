package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

object CompList:
  def append[C](list: List[C], c: C, wrapper: HTMLElement)(using
      comp: Comp[C]
  ): List[C] =
    wrapper(comp.ele(c))
    list :+ c

  def prepend[C](list: List[C], c: C, wrapper: HTMLElement)(using
      comp: Comp[C]
  ): List[C] =
    wrapper.prepend(comp.ele(c))
    c :: list

  def delete[C, T](list: List[C], pred: C => Boolean, removeElement: Boolean = false)(using
      comp: Comp[C]
  ): List[C] =
    val (pre, post) = list.span(!pred(_))
    if post.isEmpty then list
    else
      if removeElement then
        comp.ele(post.head).remove()
      pre ++ post.tail

def clear[C](list: List[C], removeElements: Boolean = false)(using comp: Comp[C]): List[C] =
  if removeElements then
    list.foreach(c => comp.ele(c).remove())
  List.empty
