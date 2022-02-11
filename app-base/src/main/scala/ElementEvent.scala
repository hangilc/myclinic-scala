package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{*, given}
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.all.{*, given}

class ElementEvent[T](using modelSymbol: ModelSymbol[T]):
  val M = modelSymbol.getSymbol()
  val C = AppModelEvent.createdSymbol
  val U = AppModelEvent.updatedSymbol
  val D = AppModelEvent.deletedSymbol
  val createdEventType = M + "-" + AppModelEvent.createdSymbol
  val createdListenerClass = createdEventType
  val createdSelector = "." + createdListenerClass
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

object ElementEvent:
  extension (ele: HTMLElement)
    def addCreatedListener[T](handler: AppModelEvent => Unit)(using
        ModelSymbol[T]
    ): Unit =
      val ee = new ElementEvent[T]
      ele(
        oncustomevent[AppModelEvent](ee.createdListenerClass) := (e =>
          handler(e.detail)
        )
      )
      ()

  extension (ele: HTMLElement)
    def addUpdatedListener[T](id: Int, handler: AppModelEvent => Unit)(using
        ModelSymbol[T],
        DataId[T]
    ): Unit =
      val ee = new ElementEvent[T]
      ele(
        oncustomevent[AppModelEvent](ee.updatedWithIdListenerClass(id)) := (e =>
          handler(e.detail)
        )
      )
      ()

    def addUpdatedAllListener[T](handler: AppModelEvent => Unit)(using
        ModelSymbol[T],
        DataId[T]
    ): Unit =
      val ee = new ElementEvent[T]
      ele(
        oncustomevent[AppModelEvent](ee.updatedListenerClass) := (e =>
          handler(e.detail)
        )
      )
      ()

  extension (ele: HTMLElement)
    def addDeletedListener[T](id: Int, handler: AppModelEvent => Unit)(using
        ModelSymbol[T]
    ): Unit =
      val ee = new ElementEvent[T]
      ele(
        oncustomevent[AppModelEvent](ee.deletedWithIdListenerClass(id)) := (e =>
          handler(e.detail)
        )
      )
      ()

    def addDeletedAllListener[T](handler: AppModelEvent => Unit)(using
        ModelSymbol[T]
    ): Unit =
      val ee = new ElementEvent[T]
      ele(
        oncustomevent[AppModelEvent](ee.deletedEventType) := (e =>
          handler(e.detail)
        )
      )
      ()
