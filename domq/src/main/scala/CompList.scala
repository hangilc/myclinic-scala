package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.TypeClasses.{Comp, Dispose}
import scala.math.Ordered.orderingToOrdered
import scala.language.implicitConversions

class CompListBase[C](
    remove: HTMLElement => Unit = _.remove()
)(using comp: Comp[C], disposer: Dispose[C]):
  protected var comps: List[C] = List.empty

  def list: List[C] = comps

  def remove(pred: C => Boolean): Unit =
    val (pre, post) = comps.span(!pred(_))
    if !post.isEmpty then
      val c = post.head
      remove(comp.ele(c))
      disposer.dispose(c)
      comps = pre ++ post.tail

  def find(pred: C => Boolean): Option[C] =
    comps.find(pred)

  def replace(oldC: C, newC: C): Unit =
    val (pre, post) = comps.span(_ == oldC)
    if !post.isEmpty then
      comp.ele(oldC).replaceBy(comp.ele(newC))
      disposer.dispose(oldC)
      comps = pre ++ (newC :: post.tail)

  def clear: Unit =
    comps.foreach(c => {
      val e = comp.ele(c)
      remove(e)
      disposer.dispose(c)
    })
    comps = List.empty

case class CompAppendList[C](val wrapper: HTMLElement)(using
    comp: Comp[C],
    disposer: Dispose[C]
) extends CompListBase[C]:
  def append(c: C): Unit =
    wrapper(comp.ele(c))
    comps = comps :+ c

  def +=(c: C): Unit = append(c)

  def set(cs: List[C]): Unit =
    clear
    cs.foreach(append _)

object CompAppendList:
  def apply[C](wrapper: HTMLElement, cs: List[C])(using Comp[C], Dispose[C]): CompAppendList[C] =
    val cl = new CompAppendList(wrapper)
    cl.set(cs)
    cl
    
  given [C](using comp: Comp[C], disposer: Dispose[C]): Dispose[CompAppendList[C]]
    with
    def dispose(t: CompAppendList[C]): Unit = t.clear

case class CompSortList[C: Ordering](val wrapper: HTMLElement)(
  using comp: Comp[C],
  disposer: Dispose[C]
) extends CompListBase[C]:
  def insert(c: C): Unit =
    val (pre, post) = comps.span(t => t < c)
    if post.isEmpty then wrapper(comp.ele(c))
    else comp.ele(post.head).preInsert(comp.ele(c))
    comps = pre ++ (c :: post)

  def +=(c: C): Unit = insert(c)

  def set(cs: List[C]): Unit =
    cs.foreach(insert _)

  def setSorted(cs: List[C]): Unit =
    clear
    cs.foreach(c => wrapper(comp.ele(c)))
    comps = cs


