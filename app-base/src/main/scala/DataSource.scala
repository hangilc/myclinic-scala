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

trait SyncedDataSourceCommon:
  def gen: Int
  private[appbase] def handleEvent(event: AppModelEvent): Unit
  private[appbase] def listenAt(ele: HTMLElement): Unit
  // def isDeleted: Boolean
  // def onUpdate(handler: () => Unit): Unit
  // def onDelete(handler: () => Unit): Unit

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
) extends LocalDataSource[(T1, T2)](init1, init2) with SyncedDataSourceCommon:
  val fetcher = summon[EventFetcher]
  private var g = initGen
  private val s1 = SyncedDataSource(g, init1)
  private val s2 = SyncedDataSource(g, init2)
  private val src = List[SyncedDataSourceCommon](s1, s2)

  s1.onUpdate(_ => doUpdate())
  s1.onDelete(_ => delete())
  s2.onUpdate(_ => doUpdate())
  s2.onDelete(_ => delete())

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

  s1.onUpdate(_ => doUpdate())
  s1.onDelete(_ => delete())
  s2.onUpdate(_ => doUpdate())
  s2.onDelete(_ => delete())
  s3.onUpdate(_ => doUpdate())
  s3.onDelete(_ => delete())

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
    if src.map(_.gen).min < maxGen || s1.isDeleted || s2.isDeleted then
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
    if src.map(_.gen).min < maxGen || s1.isDeleted || s2.isDeleted || s3.isDeleted then
      None
    else
      Some(s1.gen, s1.data, s2.data, s3.data)
