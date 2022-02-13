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

// object Comp:
//   def insert[C, T: Ordering](
//       sorted: List[C],
//       c: C,
//       wrapperOption: Option[HTMLElement]
//   )(using compElement: CompElement[C], compData: CompData[C, T]): List[C] =
//     val (pre, post) =
//       sorted.span(t => compData.getData(t) < compData.getData(c))
//     wrapperOption match {
//       case Some(wrapper) =>
//         if post.isEmpty then wrapper(compElement.getElement(c))
//         else
//           compElement.getElement(post.head).preInsert(compElement.getElement(c))
//       case None => ()
//     }
//     pre ++ (c :: post)

//   def delete[C, T: Ordering](
//       sorted: List[C],
//       pred: C => Boolean,
//       removeElement: Boolean = false
//   )(using compElement: CompElement[C], compData: CompData[C, T]): List[C] =
//     val (pre, post) = sorted.span(t => !pred(t))
//     if post.isEmpty then sorted
//     else
//       if removeElement then compElement.getElement(post.head).remove()
//       pre ++ post.tail

// class CompList[C, T](wrapper: HTMLElement, ctor: (Int, T) => C)(using
//     compElement: CompElement[C],
//     compData: CompData[C, T],
//     modelSymbol: ModelSymbol[T],
//     dataId: DataId[T]
// ):
//   var list: List[C] = List.empty

//   wrapper.addCreatedListener[T](event => {
//     val created = event.dataAs[T]
//     val c = ctor(event.appEventId, created)
//     append(c)
//   })

//   wrapper.addDeletedAllListener[T](event => {
//     val deleted = event.dataAs[T]
//     val id = dataId.getId(deleted)
//     list = list
//       .find(c => dataId.getId(compData.getData(c)) == id)
//       .fold(list)(d => list.filterNot(_ != d))
//   })

//   def append(c: C): Unit =
//     list = list :+ c
//     wrapper(compElement.getElement(c))
