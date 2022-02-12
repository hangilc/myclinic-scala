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
): 
  def updateUI(): Unit
  def ele: HTMLElement
  
  def getElement: HTMLElement = ele
  def currentGen: Int = gen
  def currentData: T = data
  final def getDataId(d: T): Int = dataId.getId(d)
  val id = getDataId(data)
  def getGenData: (Int, T) = (gen, data)
  private val msym = modelSymbol.getSymbol
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

object SyncedComp:
  given CompElement[SyncedComp[_]] with
    def getElement(c: SyncedComp[_]): HTMLElement = c.ele
  given syncedCompData[T]: CompData[SyncedComp[T], T] with
    def getData(c: SyncedComp[T]): T = c.currentData

abstract class SyncedComp2[T, U](
    private var gen: Int,
    private var data1: T,
    private var data2: U
)(using
    dataId1: DataId[T],
    modelSymbol1: ModelSymbol[T],
    dataId2: DataId[U],
    modelSymbol2: ModelSymbol[U],
    fetcher: EventFetcher
): 
  def updateUI(): Unit
  def ele: HTMLElement
  
  def getElement: HTMLElement = ele
  def currentGen: Int = gen
  def currentData1: T = data1
  def currentData2: U = data2
  final def getDataId1(d: T): Int = dataId1.getId(d)
  final def getDataId2(d: U): Int = dataId2.getId(d)
  def getGenData: (Int, T, U) = (gen, data1, data2)
  private val msym = modelSymbol.getSymbol
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
