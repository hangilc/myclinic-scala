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

class ModelCreatedEventPublisher[T](using ModelSymbol[T]):
  private val ee = new ElementEventCreated[T]
  def publishCreated(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(ee.createdEventType, event, false)
    document.body
      .qSelectorAll(ee.createdSelector)
      .foreach(_.dispatchEvent(ce))

class ModelEventPublisher[T](using modelSymbol: ModelSymbol[T], dataId: DataId[T])
  extends ModelCreatedEventPublisher[T]:
  private val ee = new ElementEvent[T]

  def publishUpdated(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(ee.updatedEventType, event, false)
    val updated: T = event.data.asInstanceOf[T]
    val id = dataId.getId(updated)
    document.body
      .qSelectorAll(ee.updatedWithIdSelector(id))
      .foreach(e => e.dispatchEvent(ce))
    document.body
      .qSelectorAll(ee.updatedSelector)
      .foreach(e => e.dispatchEvent(ce))

  def publishDeleted(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(ee.deletedEventType, event, false)
    val deleted: T = event.data.asInstanceOf[T]
    val id = dataId.getId(deleted)
    document.body
      .qSelectorAll(ee.deletedWithIdSelector(id))
      .foreach(e => e.dispatchEvent(ce))
    document.body
      .qSelectorAll(ee.deletedSelector)
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
