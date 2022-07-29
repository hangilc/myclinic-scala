package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.TypeClasses.{Comp, CompList, Dispose, given}
import scala.math.Ordered.orderingToOrdered
import scala.language.implicitConversions
import dev.fujiwara.domq.TypeClasses.DataProvider

abstract class CompListBase[C]()(using compList: CompList[C], disposer: Dispose[C]):
  protected var comps: List[C] = List.empty

  def add(c: C): Unit

  def list: List[C] = comps

  def remove(pred: C => Boolean): Unit =
    val (pre, post) = comps.span(!pred(_))
    if !post.isEmpty then
      val c = post.head
      removeComp(c)
      comps = pre ++ post.tail

  def remove(c: C): Unit = remove(_ == c)

  def find(pred: C => Boolean): Option[C] =
    comps.find(pred)

  def replace(oldC: C, newC: C): Unit =
    remove(oldC)
    add(newC)

  private def removeComp(c: C): Unit =
    compList.eles(c).foreach(_.remove())
    disposer.dispose(c)

  def clear(): Unit =
    comps.foreach(removeComp)
    comps = List.empty

case class CompAppendList[C](val wrapper: HTMLElement)(using
    comp: Comp[C],
    disposer: Dispose[C]
) extends CompListBase[C]:
  override def add(c: C): Unit = append(c)

  def append(c: C): Unit =
    wrapper(comp.ele(c))
    comps = comps :+ c

  def +=(c: C): Unit = append(c)

  def set(cs: List[C]): Unit =
    clear()
    cs.foreach(append _)

object CompAppendList:
  def apply[C](wrapper: HTMLElement, cs: List[C])(using Comp[C], Dispose[C]): CompAppendList[C] =
    val cl = new CompAppendList(wrapper)
    cl.set(cs)
    cl
    
  given [C](using comp: Comp[C], disposer: Dispose[C]): Dispose[CompAppendList[C]]
    with
    def dispose(t: CompAppendList[C]): Unit = t.clear()

case class CompSortList[C: Ordering](val wrapper: HTMLElement)(
  using comp: Comp[C],
  disposer: Dispose[C]
) extends CompListBase[C]:
  override def add(c: C): Unit = insert(c)

  def insert(c: C): Unit =
    val (pre, post) = comps.span(t => t < c)
    if post.isEmpty then wrapper(comp.ele(c))
    else comp.ele(post.head).preInsert(comp.ele(c))
    comps = pre ++ (c :: post)

  def +=(c: C): Unit = insert(c)

  def set(cs: List[C]): Unit =
    clear()
    cs.foreach(insert _)

  def setSorted(cs: List[C]): Unit =
    clear()
    cs.foreach(c => wrapper(comp.ele(c)))
    comps = cs

case class CompListSortList[C: Ordering](val wrapper: HTMLElement)(
  using compList: CompList[C],
  disposer: Dispose[C]
) extends CompListBase[C]:
  override def add(c: C): Unit = insert(c)
  
  def insert(c: C): Unit =
    val (pre, post) = comps.span(t => t < c || !compList.eles(t).isEmpty)
    if post.isEmpty then wrapper(compList.eles(c))
    else 
      val a = compList.eles(post.head).head
      compList.eles(c).foreach(e => a.preInsert(e))
    comps = pre ++ (c :: post)

  def +=(c: C): Unit = insert(c)

  def set(cs: List[C]): Unit =
    clear()
    cs.foreach(insert _)

  def setSorted(cs: List[C]): Unit =
    clear()
    cs.foreach(c => wrapper(compList.eles(c)))
    comps = cs

case class CompSortDataList[C, D: Ordering](val wrapper: HTMLElement, ctor: D => C)(
  using comp: Comp[C], dataProvider: DataProvider[C, D], disposer: Dispose[C]
):
  given Ordering[C] = Ordering.by[C, D](c => dataProvider.getData(c))
  val sortList = CompSortList[C](wrapper)
  export sortList.{wrapper => _, *}

  def sync(dataList: List[D]): Unit =
    val dataSet: Set[D] = Set.from(dataList)
    val compSet: Set[D] = Set.from(list.map(c => dataProvider.getData(c)))
    compSet.diff(dataSet).foreach(d => remove(c => dataProvider.getData(c) == d))
    dataSet.diff(compSet).foreach(d => insert(ctor(d)))





