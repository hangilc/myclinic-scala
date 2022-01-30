package dev.myclinic.scala.web.appoint.sheet

import org.scalajs.dom.HTMLElement
import scala.math.Ordered
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.ElementQ.*
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions

object Types:
  def insert[T: Ordering](instance: T, element: T => HTMLElement, sortedList: List[T], wrapper: HTMLElement): List[T] =
    val (pre, post) = sortedList.span(t => t < instance)
    if post.isEmpty then wrapper(element(instance))
    else element(post.head).preInsert(element(instance))
    pre ++ (instance +: post)

  def delete[T: Ordering](pred: T => Boolean, element: T => HTMLElement, sortedList: List[T], wrapper: HTMLElement): List[T] =
      val (pre, post) = sortedList.span(t => !pred(t))
      if post.isEmpty then sortedList
      else
        element(post.head).remove()
        pre ++ post.tail

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
