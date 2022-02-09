package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

case class EventSubscriber[T](handler: (Int, T) => Unit)

case class EventPublisher[T <: AppModelEvent](
    var subscribers: Set[EventSubscriber[T]] = Set.empty[EventSubscriber[T]]
):
  def subscribe(handler: (Int, T) => Unit): EventSubscriber[T] =
    val sub = EventSubscriber(handler)
    subscribers = subscribers + sub
    sub

  def publish(gen: Int, event: T): Unit =
    subscribers.foreach(_.handler(gen, event))

case class RealTimeEventPublisher[T](
    var handlers: Set[T => Unit] = Set.empty[T => Unit]
):
  def addHandler(handler: T => Unit): Unit =
    handlers = handlers + handler

  def publish(event: T): Unit = handlers.foreach(_(event))

class ModelPublishers[
    T,
    C <: AppModelEvent,
    U <: AppModelEvent,
    D <: AppModelEvent
](val code: String, val updateId: U => Int, val deleteId: D => Int):
  type ModelType = T
  val created = EventPublisher[C]()
  val updated = EventPublisher[U]()
  val deleted = EventPublisher[D]()
  val createdEventType = code + "-created"
  val updatedEventType = code + "-updated"
  val deletedEventType = code + "-deleted"
  val createdListenerClass = createdEventType
  val updatedListenerClass = updatedEventType
  def updatedWithIdListenerClass(id: Int) =
    updatedListenerClass + "-" + id.toString
  val deletedListenerClass = deletedEventType
  def deletedWithIdListenerClass(id: Int) =
    deletedListenerClass + "-" + id.toString
  val createdSelector = "." + createdListenerClass
  val updatedSelector = "." + updatedListenerClass
  def updatedWithIdSelector(id: Int) = "." + updatedWithIdListenerClass(id)
  def deletedSelector = "." + deletedListenerClass
  def deletedWithIdSelector(id: Int) = "." + deletedWithIdListenerClass(id)

class EventPublishers:
  val appoint =
    ModelPublishers[Appoint, AppointCreated, AppointUpdated, AppointDeleted](
      "appoint",
      _.updated.appointId,
      _.deleted.appointId
    )
  val appointTime =
    ModelPublishers[
      AppointTime,
      AppointTimeCreated,
      AppointTimeUpdated,
      AppointTimeDeleted
    ]("appoint-time", _.updated.appointTimeId, _.deleted.appointTimeId)
  val wqueue =
    ModelPublishers[Wqueue, WqueueCreated, WqueueUpdated, WqueueDeleted](
      "wqueue",
      _.updated.visitId,
      _.deleted.visitId
    )
  val shahokokuho =
    ModelPublishers[
      Shahokokuho,
      ShahokokuhoCreated,
      ShahokokuhoUpdated,
      ShahokokuhoDeleted
    ]("shahokokuho", _.updated.shahokokuhoId, _.deleted.shahokokuhoId)
  val koukikourei =
    ModelPublishers[
      Koukikourei,
      KoukikoureiCreated,
      KoukikoureiUpdated,
      KoukikoureiDeleted
    ]("koukikourei", _.updated.koukikoureiId, _.deleted.koukikoureiId)
  val roujin =
    ModelPublishers[Roujin, RoujinCreated, RoujinUpdated, RoujinDeleted](
      "roujin",
      _.updated.roujinId,
      _.deleted.roujinId
    )
  val kouhi =
    ModelPublishers[Kouhi, KouhiCreated, KouhiUpdated, KouhiDeleted](
      "kouhi",
      _.updated.kouhiId,
      _.deleted.kouhiId
    )
  val hotlineCreated = EventPublisher[HotlineCreated]()
  val hotlineBeep = RealTimeEventPublisher[HotlineBeep]()

  def publish(event: AppModelEvent, gen: Int): Unit =
    event match {
      case e: AppointCreated     => appoint.created.publish(gen, e)
      case e: AppointUpdated     => appoint.updated.publish(gen, e)
      case e: AppointDeleted     => appoint.deleted.publish(gen, e)
      case e: AppointTimeCreated => appointTime.created.publish(gen, e)
      case e: AppointTimeUpdated => appointTime.updated.publish(gen, e)
      case e: AppointTimeDeleted => appointTime.deleted.publish(gen, e)
      case e: WqueueCreated      => wqueue.created.publish(gen, e)
      case e: WqueueUpdated      => wqueue.updated.publish(gen, e)
      case e: WqueueDeleted      => wqueue.deleted.publish(gen, e)
      case e: ShahokokuhoCreated => shahokokuho.created.publish(gen, e)
      case e: ShahokokuhoUpdated => shahokokuho.updated.publish(gen, e)
      case e: ShahokokuhoDeleted => shahokokuho.deleted.publish(gen, e)
      case e: KoukikoureiCreated => koukikourei.created.publish(gen, e)
      case e: KoukikoureiUpdated => koukikourei.updated.publish(gen, e)
      case e: KoukikoureiDeleted => koukikourei.deleted.publish(gen, e)
      case e: RoujinCreated      => roujin.created.publish(gen, e)
      case e: RoujinUpdated      => roujin.updated.publish(gen, e)
      case e: RoujinDeleted      => roujin.deleted.publish(gen, e)
      case e: KouhiCreated      => kouhi.created.publish(gen, e)
      case e: KouhiUpdated      => kouhi.updated.publish(gen, e)
      case e: KouhiDeleted      => kouhi.deleted.publish(gen, e)
      case e: HotlineCreated     => hotlineCreated.publish(gen, e)
      case _                     => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
