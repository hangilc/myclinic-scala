package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.TypeClasses.{Comp, Dispose}

case class CompList[C]()(using
    comp: Comp[C],
    appender: CompList.Append[HTMLElement],
    remover: CompList.Remove[HTMLElement],
    disposer: Dispose[C],
    replacer: CompList.Replace[HTMLElement]
):
  private var comps: List[C] = List.empty
  def append(c: C): Unit =
    appender.append(comp.ele(c))
    comps = comps :+ c

  def remove(pred: C => Boolean): Unit =
    val (pre, post) = comps.span(pred(_))
    if !post.isEmpty then
      val c = post.head
      remover.remove(comp.ele(c))
      disposer.dispose(c)
      comps = pre ++ post.tail

  def find(pred: C => Boolean): Option[C] =
    comps.find(pred)

  def replace(oldC: C, newC: C): Unit =
    val (pre, post) = comps.span(_ == oldC)
    if !post.isEmpty then
      replacer.replace(comp.ele(oldC), comp.ele(newC))
      disposer.dispose(oldC)
      comps = pre ++ (newC :: post.tail)

  def clear: Unit =
    comps.foreach(c => {
      val e = comp.ele(c)
      remover.remove(e)
      disposer.dispose(c)
    })
    comps = List.empty

  def set(cs: List[C]): Unit =
    clear
    cs.foreach(append _)

object CompList:
  given [C](using
      Comp[C],
      Append[HTMLElement],
      Remove[HTMLElement],
      Dispose[C],
      Replace[HTMLElement]
  ): Dispose[CompList[C]] = _.clear

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

  trait Replace[E]:
    def replace(oldE: E, newE: E): Unit

  object Replace:
    given Replace[HTMLElement] = (a, b) => a.replaceBy(b)
