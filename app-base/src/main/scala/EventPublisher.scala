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

// class ModelEventPublisherOrig[T](val code: String):
//   type ModelType = T
//   val created = EventPublisher[TypedModelEvent[T]]()
//   val updated = EventPublisher[TypedModelEvent[T]]()
//   val deleted = EventPublisher[TypedModelEvent[T]]()
//   val createdEventType = code + "-created"
//   val updatedEventType = code + "-updated"
//   val deletedEventType = code + "-deleted"
//   val createdListenerClass = createdEventType
//   val updatedListenerClass = updatedEventType
//   def updatedWithIdListenerClass(id: Int) =
//     updatedListenerClass + "-" + id.toString
//   val deletedListenerClass = deletedEventType
//   def deletedWithIdListenerClass(id: Int) =
//     deletedListenerClass + "-" + id.toString
//   val createdSelector = "." + createdListenerClass
//   val updatedSelector = "." + updatedListenerClass
//   def updatedWithIdSelector(id: Int) = "." + updatedWithIdListenerClass(id)
//   def deletedSelector = "." + deletedListenerClass
//   def deletedWithIdSelector(id: Int) = "." + deletedWithIdListenerClass(id)

class ModelEventPublisher[T](using modelSymbol: ModelSymbol[T], dataId: DataId[T]):
  val M = modelSymbol.getSymbol()
  val C = AppModelEvent.createdSymbol
  val U = AppModelEvent.updatedSymbol
  val D = AppModelEvent.deletedSymbol
  val createdEventType = M + "-" + AppModelEvent.createdSymbol
  val updatedEventType = M + "-" + AppModelEvent.updatedSymbol
  val deletedEventType = M + "-" + AppModelEvent.deletedSymbol
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

  def publishCreated(event: AppModelEvent): Unit =
    val ce: CustomEvent[AppModelEvent] =
      CustomEvent(createdEventType, event, false)
    document.body
      .qSelectorAll(createdSelector)
      .foreach(_.dispatchEvent(ce))

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
  val hotlineCreated = new EventPublisher[HotlineCreated]
  val hotlineBeep = new RealTimeEventPublisher[HotlineBeep]()

  def publish(event: AppModelEvent): Unit =
    val C = AppModelEvent.createdSymbol
    val U = AppModelEvent.updatedSymbol
    val D = AppModelEvent.deletedSymbol

    (event.model, event.kind) match {
      case (Appoint.modelSymbol, C) => appoint.publishCreated(event)
      case (Appoint.modelSymbol, U) => appoint.publishUpdated(event)
      case (Appoint.modelSymbol, D) => appoint.publishDeleted(event)
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
      case _ => ()
    }
  def publish(event: HotlineBeep): Unit = hotlineBeep.publish(event)
