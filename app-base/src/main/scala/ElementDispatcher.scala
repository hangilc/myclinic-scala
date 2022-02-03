package dev.myclinic.scala.web.appbase

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document
import dev.myclinic.scala.model.AppModelEvent
import dev.fujiwara.domq.all.{*, given}

object ElementDispatcher:
  extension [T, C <: AppModelEvent, U <: AppModelEvent, D <: AppModelEvent](
      p: ModelPublishers[T, C, U, D]
  )
    def addDispatchers()
        : (EventSubscriber[C], EventSubscriber[U], EventSubscriber[D]) =
      (
        addCreatedDispatcher(),
        addUpdatedDispatcher(),
        addDeletedDispatcher()
      )

    def addCreatedDispatcher(): EventSubscriber[C] =
      p.created.subscribe((created, gen) => {
        val evt: CustomEvent[(C, Int)] =
          CustomEvent(p.createdEventType, (created, gen), false)
        document.body
          .qSelectorAll(p.createdSelector)
          .foreach(e => {
            e.dispatchEvent(evt)
          })
      })

    def addUpdatedDispatcher(): EventSubscriber[U] =
      p.updated.subscribe((updated, gen) => {
        val evt: CustomEvent[(U, Int)] =
          CustomEvent(p.updatedEventType, (updated, gen), false)
        document.body
          .qSelectorAll(p.updatedWithIdSelector(p.updateId(updated)))
          .foreach(e => e.dispatchEvent(evt))
        document.body
          .qSelectorAll(p.updatedSelector)
          .foreach(e => e.dispatchEvent(evt))
      })

    def addDeletedDispatcher(): EventSubscriber[D] =
      p.deleted.subscribe((deleted, gen) => {
        val evt: CustomEvent[(D, Int)] =
          CustomEvent(p.deletedEventType, (deleted, gen), false)
        document.body
          .qSelectorAll(p.deletedWithIdSelector(p.deleteId(deleted)))
          .foreach(e => e.dispatchEvent(evt))
        document.body
          .qSelectorAll(p.deletedSelector)
          .foreach(e => e.dispatchEvent(evt))
      })

  extension (ele: HTMLElement)
    def addCreatedHandler[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (C, Int) => Unit
    ): Unit =
      ele(
        oncustomevent[(C, Int)](publisher.createdEventType) := (ev =>
          handler.tupled(ev.detail)
        )
      )

    def addCreatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (C, Int) => Unit
    ): Unit =
      ele(cls := publisher.createdListenerClass)
      addCreatedHandler(publisher, handler)

    def removeCreatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D]
    ): Unit =
      ele(cls :- publisher.createdListenerClass)

    def addUpdatedHandler[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (U, Int) => Unit
    ): Unit =
      ele(
        oncustomevent[(U, Int)](publisher.updatedEventType) := (ev =>
          handler.tupled(ev.detail)
        )
      )

    def addUpdatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (U, Int) => Unit
    ): Unit =
      ele(cls := publisher.updatedListenerClass)
      addUpdatedHandler(publisher, handler)

    def removeUpdatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D]
    ): Unit =
      ele(cls :- publisher.updatedListenerClass)

    def addUpdatedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int,
        handler: (U, Int) => Unit
    ): Unit =
      ele(cls := publisher.updatedWithIdListenerClass(id))
      addUpdatedHandler(publisher, handler)

    def removeUpdatedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int
    ): Unit =
      ele(cls :- publisher.updatedWithIdListenerClass(id))

    def addDeletedHandler[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (D, Int) => Unit
    ): Unit =
      ele(
        oncustomevent[(D, Int)](publisher.deletedEventType) := (ev =>
          handler.tupled(ev.detail)
        )
      )

    def addDeletedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: (D, Int) => Unit
    ): Unit =
      ele(cls := publisher.deletedListenerClass)
      addDeletedHandler(publisher, handler)

    def removeDeletedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D]
    ): Unit =
      ele(cls :- publisher.deletedListenerClass)

    def addDeletedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int,
        handler: (D, Int) => Unit
    ): Unit =
      ele(cls := publisher.deletedWithIdListenerClass(id))
      addDeletedHandler(publisher, handler)

    def removeDeletedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int
    ): Unit =
      ele(cls :- publisher.deletedWithIdListenerClass(id))
