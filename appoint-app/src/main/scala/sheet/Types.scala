package dev.myclinic.scala.web.appoint.sheet

import org.scalajs.dom.raw.HTMLElement
import scala.math.Ordered
import dev.fujiwara.domq.ElementQ.{*, given}
import scala.language.implicitConversions

object Types:
  trait SortedElement[T <: Ordered[T]]:
    def element(t: T): HTMLElement

    def insert(newInstance: T, sortedList: List[T], wrapper: HTMLElement): List[T] =
      val (pre, post) = sortedList.span(t => t < newInstance)
      if post.isEmpty then wrapper(element(newInstance))
      else element(post.head).preInsert(element(newInstance))
      pre ++ (newInstance :: post)


