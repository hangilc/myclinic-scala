package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.AppModelEvent
import org.scalajs.dom.HTMLElement

abstract class SyncedComp[T](private var gen: Int, private var data: T)(using
    publishers: EventPublishers,
    fetcher: EventFetcher
):
  def id(d: T): Int
  val filterUpdatedEvent: PartialFunction[AppModelEvent, T]
  val filterDeletedEvent: PartialFunction[AppModelEvent, T]
  def addListeners(
      publishers: EventPublishers,
      handler: (Int, AppModelEvent) => Unit
  ): Unit
  def updateUI(): Unit
  def ele: HTMLElement

  def currentGen: Int = gen
  def currentData: T = data
  private def handleEvent(g: Int, e: AppModelEvent): Unit =
    if filterUpdatedEvent.isDefinedAt(e) then
      val updated = filterUpdatedEvent(e)
      if id(updated) == id(data) then
        data = updated
        updateUI()
    if filterDeletedEvent.isDefinedAt(e) then
      val deleted = filterDeletedEvent(e)
      if id(deleted) == id(data) then ele.remove()
  def getGenData: (Int, T) = (gen, data)

  updateUI()
  fetcher.catchup(gen, handleEvent _)
  addListeners(publishers, handleEvent _)
