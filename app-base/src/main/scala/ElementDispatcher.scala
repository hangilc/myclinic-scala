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
      p.created.subscribe(created => {
        val evt: CustomEvent[C] =
          CustomEvent(p.createdEventType, created, false)
        document.body
          .qSelectorAll(p.createdSelector)
          .foreach(e => {
            e.dispatchEvent(evt)
          })
      })

    def addUpdatedDispatcher(): EventSubscriber[U] =
      p.updated.subscribe(updated => {
        val evt: CustomEvent[U] =
          CustomEvent(p.updatedEventType, updated, false)
        document.body
          .qSelectorAll(p.updatedSelector)
          .foreach(e => {
            e.dispatchEvent(evt)
          })
        document.body
          .qSelectorAll(p.updatedSelectorWithId(p.updateId(updated)))
          .foreach(e => {
            e.dispatchEvent(evt)
          })
      })

    def addDeletedDispatcher(): EventSubscriber[D] =
      p.deleted.subscribe(deleted => {
        val evt: CustomEvent[D] =
          CustomEvent(p.deletedEventType, deleted, false)
        document.body
          .qSelectorAll(p.deletedSelector)
          .foreach(e => {
            e.dispatchEvent(evt)
          })
        document.body
          .qSelectorAll(p.deletedSelectorWithId(p.deleteId(deleted)))
          .foreach(e => {
            e.dispatchEvent(evt)
          })
      })

  extension (ele: HTMLElement)
    def addCreatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: C => Unit
    ): Unit =
      ele(cls := publisher.createdSelector)
      ele(oncustomevent[C](publisher.createdEventType) := (ev => handler(ev.detail)))

    def addUpdatedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: U => Unit
    ): Unit =
      ele(cls := publisher.updatedSelector)
      ele(oncustomevent[U](publisher.updatedEventType) := (ev => handler(ev.detail)))

    def addUpdatedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int,
        handler: U => Unit
    ): Unit =
      ele(cls := publisher.updatedSelectorWithId(id))
      ele(oncustomevent[U](publisher.updatedEventType) := (ev => handler(ev.detail)))

    def addDeletedListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        handler: D => Unit
    ): Unit =
      ele(cls := publisher.deletedSelector)
      ele(oncustomevent[D](publisher.deletedEventType) := (ev => handler(ev.detail)))

    def addDeletedWithIdListener[
        T,
        C <: AppModelEvent,
        U <: AppModelEvent,
        D <: AppModelEvent
    ](
        publisher: ModelPublishers[T, C, U, D],
        id: Int,
        handler: D => Unit
    ): Unit =
      ele(cls := publisher.deletedSelectorWithId(id))
      ele(oncustomevent[D](publisher.deletedEventType) := (ev => handler(ev.detail)))
