package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.ElementEvent.*
import dev.myclinic.scala.model.ModelSymbol
import dev.myclinic.scala.model.DataId
import dev.myclinic.scala.model.AppModelEvent
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.document
import dev.fujiwara.domq.LocalEventPublisher
import dev.fujiwara.domq.DataSource
import dev.fujiwara.domq.LocalDataSource

// trait DataSource[T]:
//   def data: T
//   def onUpdate(handler: () => Unit): Unit
//   def onDelete(handler: () => Unit): Unit
//   def isDeleted: Boolean

// class LocalDataSource[T](init: T) extends DataSource[T]:
//   private var cur: T = init
//   private val onUpdatePublisher = LocalEventPublisher[Unit]
//   private val onDeletePublisher = LocalEventPublisher[Unit]
//   private var deletedFlag = false

//   def data: T = cur
//   def onUpdate(handler: () => Unit): Unit =
//     onUpdatePublisher.subscribe(_ => handler())
//   def onDelete(handler: () => Unit): Unit =
//     onDeletePublisher.subscribe(_ => handler())

//   private[appbase] def update(value: T): Unit =
//     assert(!deletedFlag)
//     cur = value
//     onUpdatePublisher.publish(())

//   private[appbase] def delete(): Unit =
//     deletedFlag = true
//     onDeletePublisher.publish(())

//   def isDeleted: Boolean = deletedFlag

trait SyncedDataSourceCommon:
  def gen: Int
  private[appbase] def handleEvent(event: AppModelEvent): Unit
  private[appbase] def listenAt(ele: HTMLElement): Unit
  def isDeleted: Boolean
  def onUpdate(handler: () => Unit): Unit
  def onDelete(handler: () => Unit): Unit

class SyncedDataSource[T](initGen: Int, init: T)(using
    fetcher: EventFetcher,
    modelSymbol: ModelSymbol[T],
    dataId: DataId[T]
) extends LocalDataSource[T](init)
    with SyncedDataSourceCommon:
  private val M = modelSymbol.getSymbol
  private val id = dataId.getId(init)
  private var g = initGen

  def genData: (Int, T) = (g, data)
  def gen: Int = g

  def startSync(ele: HTMLElement): Unit =
    catchup()
    listenAt(ele)

  def catchup(): Unit =
    fetcher.catchup(g, handleEvent _)

  private[appbase] def listenAt(ele: HTMLElement): Unit =
    if ele.parentElement == null then
      DomUtil.hook(ele)
    ele.addUpdatedListener[T](id, handleEvent _)
    ele.addDeletedListener[T](id, handleEvent _)

  private[appbase] def handleEvent(event: AppModelEvent): Unit =
    g = event.appEventId
    if event.model == M && dataId.getId(event.dataAs[T]) == id then
      if event.kind == AppModelEvent.updatedSymbol then update(event.dataAs[T])
      else if event.kind == AppModelEvent.deletedSymbol then delete()

class SyncedDataSource2[T1, T2](initGen: Int, init1: T1, init2: T2)(using
    EventFetcher,
    ModelSymbol[T1],
    DataId[T1],
    ModelSymbol[T2],
    DataId[T2]
) extends LocalDataSource[(T1, T2)](init1, init2):
  val fetcher = summon[EventFetcher]
  private var g = initGen
  private val s1 = SyncedDataSource(g, init1)
  private val s2 = SyncedDataSource(g, init2)
  private val src: List[SyncedDataSourceCommon] = List(s1, s2)
  src.foreach(s => {
    s.onUpdate(doUpdate _)
    s.onDelete(delete _)
  })

  def genData: (Int, T1, T2) = (g, s1.data, s2.data)
  def gen: Int = g

  private def doUpdate(): Unit =
    update(s1.data, s2.data)

  def startSync(ele: HTMLElement): Unit =
    catchup()
    listenAt(ele)

  def catchup(): Unit =
    fetcher.catchup(g, handleEvent _)

  def listenAt(ele: HTMLElement): Unit =
    src.foreach(_.listenAt(ele))

  private[appbase] def handleEvent(event: AppModelEvent): Unit =
    g = event.appEventId
    src.foreach(_.handleEvent(event))

class SyncedDataSource3[T1, T2, T3](
    initGen: Int,
    init1: T1,
    init2: T2,
    init3: T3
)(using
    EventFetcher,
    ModelSymbol[T1],
    DataId[T1],
    ModelSymbol[T2],
    DataId[T2],
    ModelSymbol[T3],
    DataId[T3]
) extends LocalDataSource[(T1, T2, T3)]((init1, init2, init3)):
  val fetcher = summon[EventFetcher]
  private var g = initGen
  private val s1 = SyncedDataSource(g, init1)
  private val s2 = SyncedDataSource(g, init2)
  private val s3 = SyncedDataSource(g, init3)
  private val src: List[SyncedDataSourceCommon] = List(s1, s2, s3)
  src.foreach(s => {
    s.onUpdate(doUpdate _)
    s.onDelete(delete _)
  })

  private def doUpdate(): Unit =
    update(s1.data, s2.data, s3.data)

  def genData: (Int, T1, T2, T3) = (g, s1.data, s2.data, s3.data)
  def gen: Int = g

  def startSync(ele: HTMLElement): Unit =
    catchup()
    listenAt(ele)

  def catchup(): Unit =
    fetcher.catchup(g, handleEvent _)

  def listenAt(ele: HTMLElement): Unit =
    src.foreach(_.listenAt(ele))

  private[appbase] def handleEvent(event: AppModelEvent): Unit =
    g = event.appEventId
    src.foreach(_.handleEvent(event))

object SyncedDataSource:
  // val tmpWrapper: HTMLElement = {
  //   val e = document.createElement("div").asInstanceOf[HTMLElement]
  //   e.style.display = "none"
  //   document.body.appendChild(e)
  //   e
  // }

  def syncGen[T1, T2](
      g1: Int,
      data1: T1,
      g2: Int,
      data2: T2
  )(using
      EventFetcher,
      ModelSymbol[T1],
      DataId[T1],
      ModelSymbol[T2],
      DataId[T2]
  ): Option[(Int, T1, T2)] =
    val fetcher: EventFetcher = summon[EventFetcher]
    val s1 = new SyncedDataSource(g1, data1)
    val s2 = new SyncedDataSource(g2, data2)
    val src: List[SyncedDataSourceCommon] = List(s1, s2)
    val maxGen = src.map(_.gen).max
    fetcher.catchup(
      src.map(_.gen).min,
      event =>
        src.foreach(s => if event.appEventId > s.gen then s.handleEvent(event))
    )
    if src.map(_.gen).min < maxGen || src.find(_.isDeleted).isDefined then
      None
    else
      Some(s1.gen, s1.data, s2.data)

  def syncGen3[T1, T2, T3](
      g1: Int,
      data1: T1,
      g2: Int,
      data2: T2,
      g3: Int,
      data3: T3
  )(using
      EventFetcher,
      ModelSymbol[T1],
      DataId[T1],
      ModelSymbol[T2],
      DataId[T2],
      ModelSymbol[T3],
      DataId[T3]
  ): Option[(Int, T1, T2, T3)] =
    val fetcher = summon[EventFetcher]
    val s1 = new SyncedDataSource(g1, data1)
    val s2 = new SyncedDataSource(g2, data2)
    val s3 = new SyncedDataSource(g3, data3)
    val src: List[SyncedDataSourceCommon] = List(s1, s2, s3)
    val maxGen = src.map(_.gen).max
    fetcher.catchup(
      src.map(_.gen).min,
      event =>
        src.foreach(s => if event.appEventId > s.gen then s.handleEvent(event))
    )
    if src.map(_.gen).min < maxGen || src.find(_.isDeleted).isDefined then
      None
    else
      Some(s1.gen, s1.data, s2.data, s3.data)
