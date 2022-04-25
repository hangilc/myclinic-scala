package dev.fujiwara.domq

import scala.math.Ordered.orderingToOrdered
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.TypeClasses.{Comp, Dispose}
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}

case class SortedComps[C: scala.math.Ordering](
    var comps: List[C] = List.empty
)(using
    comp: Comp[C],
    append: SortedComps.Append[HTMLElement],
    preInsert: SortedComps.PreInsert[HTMLElement],
    remove: SortedComps.Remove[HTMLElement],
    dispose: Dispose[C]
):
  def +=(c: C): Unit =
    val (pre, post) = comps.span(t => t < c)
    if post.isEmpty then append.append(comp.ele(c))
    else preInsert.preInsert(comp.ele(c), comp.ele(post.head))
    comps = pre ++ (c :: post)

  def remove(pred: C => Boolean): Unit =
    val (pre, post) = comps.span(c => !pred(c))
    if !post.isEmpty then 
      discard(post.head)
      comps = pre ++ post.tail

  private def discard(c: C): Unit =
    remove.remove(comp.ele(c))
    dispose.dispose(c)

  def clear: Unit =
    comps.foreach(discard _)
    comps = List.empty

  def list: List[C] = comps

object SortedComps:
  trait Append[E]:
    def append(e: E): Unit

  object Append:
    def apply(wrapper: HTMLElement): Append[HTMLElement] =
      new Append[HTMLElement]:
        def append(e: HTMLElement): Unit = wrapper(e)

    def nop[E]: Append[E] = _ => ()

  trait PreInsert[E]:
    def preInsert(e: E, anchor: E): Unit

  object PreInsert:
    def apply(wrapper: HTMLElement): PreInsert[HTMLElement] =
      new PreInsert[HTMLElement]:
        def preInsert(e: HTMLElement, anchor: HTMLElement): Unit =
          anchor.preInsert(e)

    def nop[E]: PreInsert[E] = (_, _) => ()

  trait Remove[E]:
    def remove(e: E): Unit

  object Remove:
    given Remove[HTMLElement] = _.remove()

    def nop[E]: Remove[E] = _ => ()
