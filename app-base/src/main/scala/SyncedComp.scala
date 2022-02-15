package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{AppModelEvent, DataId}
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.model.ModelSymbol
import dev.myclinic.scala.web.appbase.ElementEvent
import dev.myclinic.scala.web.appbase.ElementEvent.*
import cats.*
import cats.syntax.all.*

trait DataSource[T]:
  def data: T
  val onUpdated: LocalEventPublisher[Unit]
  val onDeleted: LocalEventPublisher[Unit]

class SimpleDataSource[T](value: T) extends DataSource[T]:
  def data: T = value
  val onUpdated = LocalEventPublisher[Unit]
  val onDeleted = LocalEventPublisher[Unit]

class SyncedDataSource[T](
    gen: Int,
    initialValue: T,
    ele: HTMLElement,
    val onUpdated: LocalEventPublisher[Unit],
    val onDeleted: LocalEventPublisher[Unit]
)(using
    fetcher: EventFetcher,
    modelSymbol: ModelSymbol[T],
    dataId: DataId[T]
) extends DataSource[T]:
  private var g: Int = gen
  private var t: T = initialValue
  private val M = modelSymbol.getSymbol
  private val id: Int = dataId.getId(initialValue)
  def data: T = t

  private def handleEvent(event: AppModelEvent): Unit =
    g = event.appEventId
    event.model match {
      case M =>
        event.kind match {
          case AppModelEvent.updatedSymbol =>
            val updated = event.dataAs[T]
            if dataId.getId(updated) == id then
              t = updated
              onUpdated.publish(())
          case AppModelEvent.deletedSymbol =>
            onDeleted.publish(())
        }
      case _ => ()
    }

  fetcher.catchup(gen, handleEvent _)
  ele.addUpdatedListener[T](id, handleEvent _)
  ele.addDeletedListener[T](id, handleEvent _)

object SyncedDataSource:
  def composite[T1, T2](g1: Int, t1: T1, g2: Int, t2: T2)(using
    ModelSymbol[T1], DataId[T1], ModelSymbol[T2], DataId[T2], EventFetcher
  ): Option[SyncedDataSource[(T1, T2)]] =
    SyncedData.update2(g1, t1, g2, t2) {
      case (g, opt1, opt2)
    }
    ???

case class SyncedData[T](var data: Option[T], model: String, id: Int)(using
    dataId: DataId[T]
):
  def updateWith(event: AppModelEvent): Unit =
    if event.model == model then
      val d = event.dataAs[T]
      val i = dataId.getId(d)
      if i == id then
        event.kind match {
          case AppModelEvent.updatedSymbol => data = data.map(_ => d)
          case AppModelEvent.deletedSymbol => data = None
        }

object SyncedData:
  def update[T](gen: Int, data: T)(using
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T],
      fetcher: EventFetcher
  ): (Int, Option[T]) =
    val m = modelSymbol.getSymbol
    val id = dataId.getId(data)
    var g = gen
    var syncedData = SyncedData(Some(data), m, id)
    fetcher.catchup(g, event => syncedData.updateWith(event))
    (g, syncedData.data)

  def update2[T, U](gen1: Int, data1: T, gen2: Int, data2: U)(using
      modelSymbol1: ModelSymbol[T],
      dataId1: DataId[T],
      modelSymbol2: ModelSymbol[U],
      dataId2: DataId[U],
      fetcher: EventFetcher
  ): (Int, Option[T], Option[U]) =
    val m1 = modelSymbol1.getSymbol
    val id1 = dataId1.getId(data1)
    val m2 = modelSymbol2.getSymbol
    val id2 = dataId2.getId(data2)
    var g = gen1.min(gen2)
    var syncedData1 = SyncedData(Some(data1), m1, id1)
    var syncedData2 = SyncedData(Some(data2), m2, id2)
    fetcher.catchup(
      g,
      event =>
        if event.appEventId > gen1 then syncedData1.updateWith(event)
        if event.appEventId > gen2 then syncedData2.updateWith(event)
    )
    (g, syncedData1.data, syncedData2.data)

trait SyncedComp[C, T]:
  def create(gen: Int, data: T): C
  def ele(c: C): HTMLElement
  def updateUI(c: C, gen: Int, data: T): Unit
  def onDeleted(c: C, parent: HTMLElement): Unit

  def updateUI(c: C, gen: Int, dataOption: Option[T]): Unit =
    dataOption match {
      case Some(d) => updateUI(c, gen, d)
      case None    => ele(c).remove()
    }

object SyncedComp:
  def createSynced[C, T](gen: Int, data: T)(using
      syncedComp: SyncedComp[C, T],
      fetcher: EventFetcher,
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T]
  ): Option[C] =
    val (g, dOption) = SyncedData.update[T](gen, data)
    val id = dataId.getId(data)
    dOption match {
      case Some(d) =>
        val c = syncedComp.create(g, d)
        syncedComp
          .ele(c)
          .addUpdatedListener[T](
            id,
            event => {
              syncedComp.updateUI(c, event.appEventId, event.dataAs[T])
            }
          )
        syncedComp
          .ele(c)
          .addDeletedListener[T](
            id,
            event => {
              val parent = syncedComp.ele(c).parentElement
              syncedComp.ele(c).remove()
              syncedComp.onDeleted(c, parent)
            }
          )
        Some(c)
      case None => None
    }

trait SyncedComp2[C, T, U]:
  def create(gen: Int, data1: T, data2: U): C
  def ele(c: C): HTMLElement
  def updateUI(c: C, gen: Int, data1: T, data2: U): Unit
  def onDeleted(c: C, parent: HTMLElement): Unit

  def updateUI(
      c: C,
      gen: Int,
      data1Option: Option[T],
      data2Option: Option[U]
  ): Unit =
    (data1Option, data2Option).tupled match {
      case Some(d1, d2) => updateUI(c, gen, d1, d2)
      case None         => ele(c).remove()
    }

object SyncedComp2:
  def createSynced[C, T, U](gen1: Int, data1: T, gen2: Int, data2: U)(using
      syncedComp: SyncedComp2[C, T, U],
      fetcher: EventFetcher,
      modelSymbol1: ModelSymbol[T],
      dataId1: DataId[T],
      modelSymbol2: ModelSymbol[U],
      dataId2: DataId[U]
  ): Option[C] =
    val (g, d1Option, d2Option) =
      SyncedData.update2[T, U](gen1, data1, gen2, data2)
    val id1 = dataId1.getId(data1)
    val id2 = dataId2.getId(data2)
    (d1Option, d2Option).tupled match {
      case Some(d1, d2) =>
        val c = syncedComp.create(g, d1, d2)
        syncedComp
          .ele(c)
          .addUpdatedListener[T](
            id1,
            event => {
              val (gg, d1Option, d2Option) = SyncedData
                .update2[T, U](event.appEventId, event.dataAs[T], g, d2)
              syncedComp.updateUI(c, gg, d1Option, d2Option)
            }
          )
        syncedComp
          .ele(c)
          .addUpdatedListener[U](
            id2,
            event => {
              val (gg, d1Option, d2Option) = SyncedData
                .update2[T, U](g, d1, event.appEventId, event.dataAs[U])
              syncedComp.updateUI(c, gg, d1Option, d2Option)
            }
          )
        syncedComp
          .ele(c)
          .addDeletedListener[T](
            id1,
            event => {
              val parent = syncedComp.ele(c).parentElement
              syncedComp.ele(c).remove()
              syncedComp.onDeleted(c, parent)
            }
          )
        syncedComp
          .ele(c)
          .addDeletedListener[U](
            id2,
            event => {
              val parent = syncedComp.ele(c).parentElement
              syncedComp.ele(c).remove()
              syncedComp.onDeleted(c, parent)
            }
          )
        Some(c)
      case None => None
    }
