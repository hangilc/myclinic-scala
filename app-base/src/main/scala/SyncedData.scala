package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.{ModelSymbol, AppModelEvent, DataId}

object SyncedData:
  def updateDataWithEvent[T](data: Option[T], id: Int, event: AppModelEvent)(
    using modelSymbol: ModelSymbol[T], dataId: DataId[T] 
  ): Option[T] =
    val M = modelSymbol.getSymbol
    event.model match {
      case M => 
        val d = event.dataAs[T]
        event.kind match {
          case AppModelEvent.createdSymbol | AppModelEvent.updatedSymbol => Some(d)
          case AppModelEvent.deletedSymbol => None
          case _ => data
        }
      case _ => data
    }

  def updateData[T](genFrom: Int, genUpto: Int, origData: T)(using
      fetcher: EventFetcher,
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T]
  ):  Option[T] =
    if genFrom < genUpto then
      var data: Option[T] = Some(origData)
      val id = dataId.getId(origData)
      fetcher.updateTo(genFrom, genUpto, event => {
        data = updateDataWithEvent(data, id, event)
      })
      data
    else Some(origData)

  def sync[T, U](gen1: Int, data1: T, gen2: Int, data2: U)(using
      fetcher: EventFetcher,
      modelSymbolT: ModelSymbol[T],
      modelSymbolU: ModelSymbol[U],
      dataIdT: DataId[T],
      dataIdU: DataId[U]
  ): (Int, Option[T], Option[U]) =
    val gen = gen1.max(gen2)
    (gen, updateData(gen1, gen, data1), updateData(gen2, gen, data2))