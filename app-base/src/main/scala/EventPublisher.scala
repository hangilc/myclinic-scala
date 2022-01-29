package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

// trait EventSubscriberController:
//   def start(): Unit
//   def stop(): Unit

// case class EventSubscriber[T <: AppModelEvent](
//     private val handler: (T, Int) => Unit,
//     publisher: EventPublisher[T]
// ) extends EventSubscriberController:
//   var isStopped: Boolean = true
//   val queue = mutable.Queue[(T, AppEvent)]()

//   def handle(event: T, appEvent: AppEvent): Unit =
//     queue.append((event, appEvent))
//     if !isStopped then handleQueue()

//   def start(): Unit =
//     isStopped = false
//     handleQueue()

//   def stop(): Unit =
//     isStopped = true

//   def unsubscribe(): Unit =
//     publisher.unsubscribe(this)

//   private def handleQueue(): Unit =
//     while !queue.isEmpty do
//       val (event, appEvent) = queue.dequeue()
//       handleOne(event, appEvent)

//   private def handleOne(event: T, appEvent: AppEvent): Unit =
//     try handler(event, appEvent)
//     catch {
//       case e: Throwable => System.err.println(e.toString)
//     }

case class EventSubscriber[T](handler: (T, Int) => Unit)

case class EventPublisher[T <: AppModelEvent](
    var subscribers: Set[EventSubscriber[T]] = Set.empty[EventSubscriber[T]]
):
  def subscribe(handler: (T, Int) => Unit): EventSubscriber[T] =
    val sub = EventSubscriber(handler)
    subscribers = subscribers + sub
    sub

  def publish(event: T, gen: Int): Unit =
    subscribers.foreach(_.handler(event, gen))

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
  val createdSelector = "." + createdEventType
  val updatedSelector = "." + updatedEventType
  def updatedSelectorWithId(id: Int) = updatedSelector + "-" + id.toString
  def deletedSelector = "." + deletedEventType
  def deletedSelectorWithId(id: Int) = deletedSelector + "-" + id.toString

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
  val hotlineCreated = EventPublisher[HotlineCreated]()
  val hotlineBeep = RealTimeEventPublisher[HotlineBeep]()

  def publish(event: AppModelEvent, gen: Int): Unit =
    event match {
      case e: AppointCreated     => appoint.created.publish(e, gen)
      case e: AppointUpdated     => appoint.updated.publish(e, gen)
      case e: AppointDeleted     => appoint.deleted.publish(e, gen)
      case e: AppointTimeCreated => appointTime.created.publish(e, gen)
      case e: AppointTimeUpdated => appointTime.updated.publish(e, gen)
      case e: AppointTimeDeleted => appointTime.deleted.publish(e, gen)
      case e: WqueueCreated      => wqueue.created.publish(e, gen)
      case e: WqueueUpdated      => wqueue.updated.publish(e, gen)
      case e: WqueueDeleted      => wqueue.deleted.publish(e, gen)
      case e: ShahokokuhoCreated => shahokokuho.created.publish(e, gen)
      case e: ShahokokuhoUpdated => shahokokuho.updated.publish(e, gen)
      case e: ShahokokuhoDeleted => shahokokuho.deleted.publish(e, gen)
      case e: KoukikoureiCreated => koukikourei.created.publish(e, gen)
      case e: KoukikoureiUpdated => koukikourei.updated.publish(e, gen)
      case e: KoukikoureiDeleted => koukikourei.deleted.publish(e, gen)
      case e: RoujinCreated      => roujin.created.publish(e, gen)
      case e: RoujinUpdated      => roujin.updated.publish(e, gen)
      case e: RoujinDeleted      => roujin.deleted.publish(e, gen)
      case e: HotlineCreated     => hotlineCreated.publish(e, gen)
      case _                     => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
