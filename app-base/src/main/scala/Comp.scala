package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import scala.math.Ordered
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.model.ModelSymbol
import dev.myclinic.scala.model.DataId

trait Comp[C]:
  def ele(c: C): HTMLElement

trait CompData[C, T] extends Comp[C]:
  def data(c: C): T

trait DeleteNotifier[C]:
  def subscribe(c: C, handler: () => Unit): Unit

class ListOfComp[C](wrapper: HTMLElement)(using
    comp: Comp[C],
    deleteNotifier: DeleteNotifier[C]
):
  protected var store: List[C] = List.empty
  def append(c: C): Unit =
    subscribe(c)
    wrapper(comp.ele(c))
  def prepend(c: C): Unit =
    subscribe(c)
    wrapper.prepend(comp.ele(c))
  def list: List[C] = store
  def contains(pred: C => Boolean): Boolean = store.find(pred(_)).isDefined
  def delete(c: C): Unit =
    delete((item: C) => item == c)
  def delete(pred: C => Boolean): Unit =
    val (dels, eles) = store.partition(pred)
    dels.foreach(d => comp.ele(d).remove())
    store = eles
  protected def subscribe(c: C): Unit =
    deleteNotifier.subscribe(
      c,
      () => {
        store = store.filter(_ != c)
      }
    )

class ListOfSortedComp[C](wrapper: HTMLElement)(using
    ordering: Ordering[C],
    comp: Comp[C],
    deleteNotifier: DeleteNotifier[C]
) extends ListOfComp[C](wrapper):
  def insert(c: C): Unit =
    subscribe(c)
    store = SortedCompList.insert(store, c, wrapper)
  def set(cs: List[C]): Unit =
    wrapper(clear)
    store = cs.sorted
    store.foreach(c => {
      subscribe(c)
      wrapper(comp.ele(c))
    })
