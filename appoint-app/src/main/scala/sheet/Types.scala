package dev.myclinic.scala.web.appoint.sheet

import org.scalajs.dom.raw.HTMLElement
import scala.math.Ordered
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.ElementQ.{*, given}
import scala.language.implicitConversions

object Types:
  trait SortedElement[T: Ordering]:
    def element(t: T): HTMLElement

    def insert(
        newInstance: T,
        sortedList: Seq[T],
        wrapper: HTMLElement
    ): Seq[T] =
      val (pre, post) = sortedList.span(t => t < newInstance)
      if post.isEmpty then wrapper(element(newInstance))
      else element(post.head).preInsert(element(newInstance))
      pre ++ (newInstance +: post)

    def remove(pred: T => Boolean, sortedList: Seq[T]): Seq[T] =
      val (pre, post) = sortedList.span(t => !pred(t))
      if post.isEmpty then sortedList
      else
        element(post.head).remove()
        pre ++ post.tail

    def update(pred: T => Boolean, updated: T, sortedList: Seq[T]): Seq[T] = 
      val (pre, post) = sortedList.span(t => !pred(t))
      if post.isEmpty then sortedList
      else
        element(post.head).replaceBy(element(updated))
        (pre :+ updated) ++ post.tail