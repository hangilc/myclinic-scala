package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{AppModelEvent, DataId}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.ModelSymbol
import dev.myclinic.scala.web.appbase.ElementEvent
import dev.myclinic.scala.web.appbase.ElementEvent.*

abstract class SyncedComp[T](
    private var gen: Int,
    private var data: T
)(using
    dataId: DataId[T],
    modelSymbol: ModelSymbol[T],
    fetcher: EventFetcher
) extends Comp: 
  def updateUI(): Unit
  def ele: HTMLElement
  
  def getElement: HTMLElement = ele
  def currentGen: Int = gen
  def currentData: T = data
  final def getDataId(d: T): Int = dataId.getId(d)
  val id = getDataId(data)
  def getGenData: (Int, T) = (gen, data)
  private val msym = modelSymbol.getSymbol()
  private def handleEvent(e: AppModelEvent): Unit =
    if e.model == msym && e.kind == AppModelEvent.updatedSymbol then
      val updated = e.dataAs[T]
      if getDataId(updated) == id then
        data = updated
        updateUI()
    if e.model == msym && e.kind == AppModelEvent.deletedSymbol then
      val deleted = e.dataAs[T]
      if dataId.getId(deleted) == id then ele.remove()
    gen = e.appEventId

  def initSyncedComp(): Unit =
    updateUI()
    fetcher.catchup(gen, handleEvent _)
    ele.addUpdatedListener[T](id, handleEvent _)
    ele.addDeletedListener[T](id, handleEvent _)

