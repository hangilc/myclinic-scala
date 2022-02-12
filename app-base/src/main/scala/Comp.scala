package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import scala.math.Ordered
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.all.{*, given}

trait Comp:
  def getElement: HTMLElement

object Comp:
  def insert[C <: Comp : Ordering](
      sorted: List[C],
      c: C,
      wrapperOption: Option[HTMLElement]
  ): List[C] =
    val (pre, post) = sorted.span(t => t < c)
    wrapperOption match {
      case Some(wrapper) => 
        if post.isEmpty then wrapper(c.getElement)
        else post.head.getElement.preInsert(c.getElement)
      case None => ()
    }
    pre ++ (c :: post)

  def delete[T, C <: Comp : Ordering](
      sorted: List[C],
      pred: C => Boolean,
      removeElement: Boolean = false
  ): List[C] =
    val (pre, post) = sorted.span(t => !pred(t))
    if post.isEmpty then sorted
    else
      if removeElement then
        post.head.getElement.remove()
      pre ++ post.tail

