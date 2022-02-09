package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{AppModelEvent, DataId}
import org.scalajs.dom.HTMLElement

abstract class SyncedComp[T](
    private var gen: Int,
    private var data: T
)(using
    dataId: DataId[T],
    publishers: EventPublishers,
    fetcher: EventFetcher
):
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
  final def getDataId(d: T): Int = dataId.getId(d)
  private def handleEvent(g: Int, e: AppModelEvent): Unit =
    if filterUpdatedEvent.isDefinedAt(e) then
      val updated = filterUpdatedEvent(e)
      if getDataId(updated) == getDataId(data) then
        data = updated
        updateUI()
    if filterDeletedEvent.isDefinedAt(e) then
      val deleted = filterDeletedEvent(e)
      if dataId.getId(deleted) == getDataId(data) then ele.remove()
  def getGenData: (Int, T) = (gen, data)

  updateUI()
  fetcher.catchup(gen, handleEvent _)
  addListeners(publishers, handleEvent _)
