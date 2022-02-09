package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable
import org.scalajs.dom.document
import dev.fujiwara.domq.all.*

class LocalEventPublisher[T]:
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers :+ handler
  def publish(t: T): Unit = subscribers.foreach(_(t))

class ModelCreatedEventPublisher[T](using modelSymbol: ModelSymbol[T]):
  val M = modelSymbol.getSymbol()
  val C = AppModelEvent.createdSymbol
  val createdEventType = M + "-" + AppModelEvent.createdSymbol
  val createdListenerClass = createdEventType
  val createdSelector = "." + createdListenerClass
  def publishCreated(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(createdEventType, event, false)
    document.body
      .qSelectorAll(createdSelector)
      .foreach(_.dispatchEvent(ce))

class ModelEventPublisher[T](using modelSymbol: ModelSymbol[T], dataId: DataId[T])
  extends ModelCreatedEventPublisher[T]:
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

  def publishUpdated(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(updatedEventType, event, false)
    val updated: T = event.data.asInstanceOf[T]
    val id = dataId.getId(updated)
    document.body
      .qSelectorAll(updatedWithIdSelector(id))
      .foreach(e => e.dispatchEvent(ce))
    document.body
      .qSelectorAll(updatedSelector)
      .foreach(e => e.dispatchEvent(ce))

  def publishDeleted(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(deletedEventType, event, false)
    val deleted: T = event.data.asInstanceOf[T]
    val id = dataId.getId(deleted)
    document.body
      .qSelectorAll(deletedWithIdSelector(id))
      .foreach(e => e.dispatchEvent(ce))
    document.body
      .qSelectorAll(deletedSelector)
      .foreach(e => e.dispatchEvent(ce))

class EventPublishers:
  val appoint = new ModelEventPublisher[Appoint]
  val appointTime = new ModelEventPublisher[AppointTime]
  val wqueue = new ModelEventPublisher[Wqueue]
  val shahokokuho = new ModelEventPublisher[Shahokokuho]
  val koukikourei = new ModelEventPublisher[Koukikourei]
  val roujin = new ModelEventPublisher[Roujin]
  val kouhi = new ModelEventPublisher[Kouhi]
  val hotlineCreated = new ModelCreatedEventPublisher[Hotline]
  val hotlineBeep = new LocalEventPublisher[HotlineBeep]()

  def publish(event: AppModelEvent): Unit =
    val C = AppModelEvent.createdSymbol
    val U = AppModelEvent.updatedSymbol
    val D = AppModelEvent.deletedSymbol

    (event.model, event.kind) match {
      case (Appoint.modelSymbol, C) => appoint.publishCreated(event)
      case (Appoint.modelSymbol, U) => appoint.publishUpdated(event)
      case (Appoint.modelSymbol, D) => appoint.publishDeleted(event)
      case (AppointTime.modelSymbol, C) => appointTime.publishCreated(event)
      case (AppointTime.modelSymbol, U) => appointTime.publishUpdated(event)
      case (AppointTime.modelSymbol, D) => appointTime.publishDeleted(event)
      case (Wqueue.modelSymbol, C) => wqueue.publishCreated(event)
      case (Wqueue.modelSymbol, U) => wqueue.publishUpdated(event)
      case (Wqueue.modelSymbol, D) => wqueue.publishDeleted(event)
      case (Shahokokuho.modelSymbol, C) => shahokokuho.publishCreated(event)
      case (Shahokokuho.modelSymbol, U) => shahokokuho.publishUpdated(event)
      case (Shahokokuho.modelSymbol, D) => shahokokuho.publishDeleted(event)
      case (Koukikourei.modelSymbol, C) => koukikourei.publishCreated(event)
      case (Koukikourei.modelSymbol, U) => koukikourei.publishUpdated(event)
      case (Koukikourei.modelSymbol, D) => koukikourei.publishDeleted(event)
      case (Roujin.modelSymbol, C) => roujin.publishCreated(event)
      case (Roujin.modelSymbol, U) => roujin.publishUpdated(event)
      case (Roujin.modelSymbol, D) => roujin.publishDeleted(event)
      case (Kouhi.modelSymbol, C) => kouhi.publishCreated(event)
      case (Kouhi.modelSymbol, U) => kouhi.publishUpdated(event)
      case (Kouhi.modelSymbol, D) => kouhi.publishDeleted(event)
      case (Hotline.modelSymbol, C) => hotlineCreated.publishCreated(event)
      case _ => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
