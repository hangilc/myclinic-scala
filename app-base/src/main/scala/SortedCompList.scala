package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.DataId

object SortedCompList:
  def insert[C: Ordering](
      sorted: List[C],
      c: C,
      wrapper: HTMLElement
  )(using comp: Comp[C]): List[C] =
    val (pre, post) = sorted.span(t => t < c)
    if post.isEmpty then wrapper(comp.ele(c))
    else
      comp.ele(post.head).preInsert(comp.ele(c))
    pre ++ (c :: post)

  def delete[C, T](
      sorted: List[C],
      id: Int,
      removeElement: Boolean = false
  )(using compData: CompData[C, T], dataId: DataId[T]): List[C] =
    val (pre, post) = sorted.span(t => dataId.getId(compData.data(t)) != id)
    if post.isEmpty then sorted
    else
      if removeElement then
        compData.ele(post.head).remove()
      pre ++ post.tail
