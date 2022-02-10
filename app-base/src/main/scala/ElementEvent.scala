package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{*, given}
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}

class ElementEventCreated[T](using modelSymbol: ModelSymbol[T]):
  val M = modelSymbol.getSymbol()
  val C = AppModelEvent.createdSymbol
  val createdEventType = M + "-" + AppModelEvent.createdSymbol
  val createdListenerClass = createdEventType
  val createdSelector = "." + createdListenerClass

object ElementEventCreated:
  extension (ele: HTMLElement)
    def addCreatedListener[T](handler: AppModelEvent => Unit)(using ModelSymbol[T]): Unit =
      val ee = new ElementEventCreated[T]
      ele(oncustomevent[AppModelEvent](createdEventType) := handler))

class ElementEvent[T](using modelSymbol: ModelSymbol[T], dataId: DataId[T])
    extends ElementEventCreated[T]:
  val U = AppModelEvent.updatedSymbol
  val D = AppModelEvent.deletedSymbol
  val updatedEventType = M + "-" + AppModelEvent.updatedSymbol
  val deletedEventType = M + "-" + AppModelEvent.deletedSymbol
  val updatedListenerClass = updatedEventType
  def updatedWithIdListenerClass(id: Int) =
    updatedListenerClass + "-" + id.toString
  val deletedListenerClass = deletedEventType
  def deletedWithIdListenerClass(id: Int) =
    deletedListenerClass + "-" + id.toString
  val updatedSelector = "." + updatedListenerClass
  def updatedWithIdSelector(id: Int) = "." + updatedWithIdListenerClass(id)
  def deletedSelector = "." + deletedListenerClass
  def deletedWithIdSelector(id: Int) = "." + deletedWithIdListenerClass(id)

// import org.scalajs.dom.HTMLElement
// import org.scalajs.dom.document
// import dev.myclinic.scala.model.AppModelEvent
// import dev.fujiwara.domq.all.{*, given}

// object ElementDispatcher:
//   extension [T, C <: AppModelEvent, U <: AppModelEvent, D <: AppModelEvent](
//       p: ModelPublishers[T, C, U, D]
//   )
//     def addDispatchers()
//         : (EventSubscriber[C], EventSubscriber[U], EventSubscriber[D]) =
//       (
//         addCreatedDispatcher(),
//         addUpdatedDispatcher(),
//         addDeletedDispatcher()
//       )

//     def addCreatedDispatcher(): EventSubscriber[C] =
//       p.created.subscribe((gen, created) => {
//         val evt: CustomEvent[(Int, C)] =
//           CustomEvent(p.createdEventType, (gen, created), false)
//         document.body
//           .qSelectorAll(p.createdSelector)
//           .foreach(e => {
//             e.dispatchEvent(evt)
//           })
//       })

//     def addUpdatedDispatcher(): EventSubscriber[U] =
//       p.updated.subscribe((gen, updated) => {
//         val evt: CustomEvent[(Int, U)] =
//           CustomEvent(p.updatedEventType, (gen, updated), false)
//         document.body
//           .qSelectorAll(p.updatedWithIdSelector(p.updateId(updated)))
//           .foreach(e => e.dispatchEvent(evt))
//         document.body
//           .qSelectorAll(p.updatedSelector)
//           .foreach(e => e.dispatchEvent(evt))
//       })

//     def addDeletedDispatcher(): EventSubscriber[D] =
//       p.deleted.subscribe((gen, deleted) => {
//         val evt: CustomEvent[(Int, D)] =
//           CustomEvent(p.deletedEventType, (gen, deleted), false)
//         document.body
//           .qSelectorAll(p.deletedWithIdSelector(p.deleteId(deleted)))
//           .foreach(e => e.dispatchEvent(evt))
//         document.body
//           .qSelectorAll(p.deletedSelector)
//           .foreach(e => e.dispatchEvent(evt))
//       })

//   extension (ele: HTMLElement)
//     def addCreatedHandler[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, C) => Unit
//     ): Unit =
//       ele(
//         oncustomevent[(Int, C)](publisher.createdEventType) := (ev =>
//           handler.tupled(ev.detail)
//         )
//       )

//     def addCreatedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, C) => Unit
//     ): Unit =
//       ele(cls := publisher.createdListenerClass)
//       addCreatedHandler(publisher, handler)

//     def removeCreatedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D]
//     ): Unit =
//       ele(cls :- publisher.createdListenerClass)

//     def addUpdatedHandler[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, U) => Unit
//     ): Unit =
//       ele(
//         oncustomevent[(Int, U)](publisher.updatedEventType) := (ev =>
//           handler.tupled(ev.detail)
//         )
//       )

//     def addUpdatedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, U) => Unit
//     ): Unit =
//       ele(cls := publisher.updatedListenerClass)
//       addUpdatedHandler(publisher, handler)

//     def removeUpdatedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D]
//     ): Unit =
//       ele(cls :- publisher.updatedListenerClass)

//     def addUpdatedWithIdListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         id: Int,
//         handler: (Int, U) => Unit
//     ): Unit =
//       ele(cls := publisher.updatedWithIdListenerClass(id))
//       addUpdatedHandler(publisher, handler)

//     def removeUpdatedWithIdListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         id: Int
//     ): Unit =
//       ele(cls :- publisher.updatedWithIdListenerClass(id))

//     def addDeletedHandler[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, D) => Unit
//     ): Unit =
//       ele(
//         oncustomevent[(Int, D)](publisher.deletedEventType) := (ev =>
//           handler.tupled(ev.detail)
//         )
//       )

//     def addDeletedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         handler: (Int, D) => Unit
//     ): Unit =
//       ele(cls := publisher.deletedListenerClass)
//       addDeletedHandler(publisher, handler)

//     def removeDeletedListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D]
//     ): Unit =
//       ele(cls :- publisher.deletedListenerClass)

//     def addDeletedWithIdListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         id: Int,
//         handler: (Int, D) => Unit
//     ): Unit =
//       ele(cls := publisher.deletedWithIdListenerClass(id))
//       addDeletedHandler(publisher, handler)

//     def removeDeletedWithIdListener[
//         T,
//         C <: AppModelEvent,
//         U <: AppModelEvent,
//         D <: AppModelEvent
//     ](
//         publisher: ModelPublishers[T, C, U, D],
//         id: Int
//     ): Unit =
//       ele(cls :- publisher.deletedWithIdListenerClass(id))
