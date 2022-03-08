package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.DataId

class SortedCompList[C](wrapper: HTMLElement)(
  using ordering: Ordering[C], comp: Comp[C]
):
  private var store: List[C] = List.empty
  import SortedCompList as O
  def list: List[C] = store
  def set(cs: List[C]): Unit = 
    wrapper(clear)
    store = cs.sorted
    store.foreach(c => wrapper(comp.ele(c)))
  def insert(c: C): Unit =
    store = O.insert(store, c, wrapper)
  def delete(pred: C => Boolean, removeElement: Boolean = false): Unit =
    store = O.delete(store, pred, removeElement)

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

  def delete[C](
    sorted: List[C],
    pred: C => Boolean,
    removeElement: Boolean
  )(using comp: Comp[C]): List[C] = 
    val (pre, post) = sorted.span(c => !pred(c))
    if post.isEmpty then sorted
    else
      if removeElement then
        comp.ele(post.head).remove()
      pre ++ post.tail

  // def delete[C, T](
  //     sorted: List[C],
  //     id: Int,
  //     removeElement: Boolean
  // )(using compData: CompData[C, T], dataId: DataId[T]): List[C] =
  //   val (pre, post) = sorted.span(t => dataId.getId(compData.data(t)) != id)
  //   if post.isEmpty then sorted
  //   else
  //     if removeElement then
  //       compData.ele(post.head).remove()
  //     pre ++ post.tail
