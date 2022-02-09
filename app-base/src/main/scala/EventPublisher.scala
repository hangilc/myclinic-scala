package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.*
import scala.collection.mutable

class EventPublisher[T]():
  private var subscribers: List[T => Unit] = List.empty
  def subscribe(handler: T => Unit): Unit =
    subscribers = subscribers :+ handler
  def publish(t: T): Unit = subscribers.foreach(_(t))

class EventPublisher[T]:
  private var subscribers: List[(Int, T) => Unit] = List.empty
  def subscribe(handler: (Int, T) => Unit): Unit =
    subscribers = subscribers :+ handler
  def publish(gen: Int, event: T): Unit =
    subscribers.foreach(_(gen, event))

class ModelEventPublisher[T](val code: String):
  type ModelType = T
  val created = EventPublisher[TypedModelEvent[T]]()
  val updated = EventPublisher[TypedModelEvent[T]]()
  val deleted = EventPublisher[TypedModelEvent[T]]()
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
  val appoint = ModelPublishers[Appoint]("appoint")
  val appointTime = ModelPublishers[AppointTime]("appoint-time")
  val wqueue = ModelPublishers[Wqueue]("wqueue")
  val shahokokuho = ModelPublishers[Shahokokuho]("shahokokuho")
  val koukikourei = ModelPublishers[Koukikourei]("koukikourei")
  val roujin = ModelPublishers[Roujin]("roujin")
  val kouhi = ModelPublishers[Kouhi]("kouhi")
  val hotlineCreated = EventPublisher[HotlineCreated]()
  val hotlineBeep = RealTimeEventPublisher[HotlineBeep]()

  def publish(event: AppModelEvent): Unit =
    val C = AppModelEvent.createdSymbol
    val U = AppModelEvent.updatedSymbol
    val D = AppModelEvent.deletedSymbol
    def as[T]: TypedModelEvent[T] = TypedModelEvent.from[T](event)

    (event.model, event.kind) match {
      case (Appoint.modelSymbol, C) => appoint.created.publish(as[Appoint])
      case (Appoint.modelSymbol, U) => appoint.updated.publish(as[Appoint])
      case (Appoint.modelSymbol, D) => appoint.deleted.publish(as[Appoint])
      // case e: AppointUpdated     => appoint.updated.publish(gen, e)
      // case e: AppointDeleted     => appoint.deleted.publish(gen, e)
      // case e: AppointTimeCreated => appointTime.created.publish(gen, e)
      // case e: AppointTimeUpdated => appointTime.updated.publish(gen, e)
      // case e: AppointTimeDeleted => appointTime.deleted.publish(gen, e)
      // case e: WqueueCreated      => wqueue.created.publish(gen, e)
      // case e: WqueueUpdated      => wqueue.updated.publish(gen, e)
      // case e: WqueueDeleted      => wqueue.deleted.publish(gen, e)
      // case e: ShahokokuhoCreated => shahokokuho.created.publish(gen, e)
      // case e: ShahokokuhoUpdated => shahokokuho.updated.publish(gen, e)
      // case e: ShahokokuhoDeleted => shahokokuho.deleted.publish(gen, e)
      // case e: KoukikoureiCreated => koukikourei.created.publish(gen, e)
      // case e: KoukikoureiUpdated => koukikourei.updated.publish(gen, e)
      // case e: KoukikoureiDeleted => koukikourei.deleted.publish(gen, e)
      // case e: RoujinCreated      => roujin.created.publish(gen, e)
      // case e: RoujinUpdated      => roujin.updated.publish(gen, e)
      // case e: RoujinDeleted      => roujin.deleted.publish(gen, e)
      // case e: KouhiCreated       => kouhi.created.publish(gen, e)
      // case e: KouhiUpdated       => kouhi.updated.publish(gen, e)
      // case e: KouhiDeleted       => kouhi.deleted.publish(gen, e)
      // case e: HotlineCreated     => hotlineCreated.publish(gen, e)
      case _                     => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
