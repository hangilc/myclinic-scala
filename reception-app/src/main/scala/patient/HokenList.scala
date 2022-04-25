package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, CustomEvent, DataSource}
import dev.fujiwara.domq.TypeClasses.Comp
import scala.language.implicitConversions
import org.scalajs.dom.{HTMLElement, HTMLInputElement}
import scala.scalajs.js
import dev.myclinic.scala.util.{DateUtil, HokenRep}
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.{*, given}
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.{Success, Failure}
import scala.concurrent.Future
import org.scalajs.dom.Event
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.DomqUtil
import dev.myclinic.scala.web.appbase.{EventFetcher}
import dev.myclinic.scala.web.appbase.SyncedDataSource
import dev.myclinic.scala.web.appbase.ListOfSortedComp
import dev.myclinic.scala.web.appbase.DeleteNotifier
import dev.myclinic.scala.web.appbase.ElementEvent.*

class HokenList(patientId: Int)(using EventFetcher):
  import HokenList.*
  val errorBox = ErrorBox()
  val eDisp = div()
  val eListAll: HTMLInputElement = checkbox()
  val ele = div(
    errorBox.ele,
    eDisp(cls := "hoken-list-disp"),
    div(
      eListAll(
        onchange := (onListAllChange _)
      ),
      span("過去の保険も含める  ")
    )
  )
  private val list: ListOfSortedComp[Item] = ListOfSortedComp(eDisp)
  addCreatedListener[Shahokokuho]
  addCreatedListener[Koukikourei]
  addCreatedListener[Kouhi]
  addCreatedListener[Roujin]
  addUpdatedAllListener[Shahokokuho]
  addUpdatedAllListener[Koukikourei]
  addUpdatedAllListener[Kouhi]
  addUpdatedAllListener[Roujin]
  onListAllChange()

  private def isValidNow[T](t: T)(using 
      periodProvider: EffectivePeriodProvider[T],
  ): Boolean =
    periodProvider.isValidAt(t, LocalDate.now())

  private def isToBeShown[T](t: T)(using 
      periodProvider: EffectivePeriodProvider[T],
  ): Boolean =
    isListingAll || isValidNow(t)

  private def isInList[T](t: T)(using
    ModelSymbol[T],
    DataId[T] 
  ): Boolean =
    val id = HokenId(t)
    list.contains(_.id == id)

  private def addCreatedListener[T](using
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T],
      periodProvider: EffectivePeriodProvider[T],
      repProvider: RepProvider[T],
      patientIdProvider: PatientIdProvider[T]
  ): Unit =
    ele.addCreatedListener[T](event => {
      val created = event.dataAs[T]
      if patientIdProvider.getPatientId(created) == patientId then
        if isToBeShown(created) then
          val gen = event.appEventId
          val item = Item(gen, created)
          list.insert(item)
      })

  private def addUpdatedAllListener[T](using
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T],
      periodProvider: EffectivePeriodProvider[T],
      repProvider: RepProvider[T],
      patientIdProvider: PatientIdProvider[T]
  ): Unit =
    ele.addUpdatedAllListener[T](event => {
      val updated = event.dataAs[T]
      if patientIdProvider.getPatientId(updated) == patientId then
        if isToBeShown(updated) then
          if !isInList(updated) then list.insert(Item(event.appEventId, updated))
        else 
          if isInList(updated) then list.delete(_.id == HokenId(updated))
    })

  private def isListingAll: Boolean = eListAll.checked

  private def onListAllChange(): Unit =
    (for
      items <-
        if isListingAll then fetchAll()
        else fetchCurrent()
    yield list.set(items)).onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private def setHokenList(items: List[Item]): Unit = list.set(items)

  private def fetchCurrent(): Future[List[Item]] =
    Api.getPatientHoken(patientId, LocalDate.now()).map(convertToItems.tupled)

  private def fetchAll(): Future[List[Item]] =
    Api.getPatientAllHoken(patientId).map(convertToItems.tupled)

  private def convertToItems(
      gen: Int,
      _patient: Patient,
      shahokokuhoList: List[Shahokokuho],
      koukikoureiList: List[Koukikourei],
      roujinList: List[Roujin],
      kouhiList: List[Kouhi]
  ): List[Item] =
    val list: List[Item] =
      shahokokuhoList.map(shaho => Item(gen, shaho))
        ++ roujinList.map(roujin => Item(gen, roujin))
        ++ koukikoureiList.map(koukikourei => Item(gen, koukikourei))
        ++ kouhiList.map(kouhi => Item(gen, kouhi))
    list

object HokenList:
  case class HokenId(symbol: String, id: Int)

  object HokenId:
    def apply[T](
        t: T
    )(using modelSymbol: ModelSymbol[T], dataId: DataId[T]): HokenId =
      HokenId(modelSymbol.getSymbol, dataId.getId(t))

  trait Item:
    def ele: HTMLElement
    def validFrom: LocalDate
    def id: HokenId
    def onDelete(handler: () => Unit): Unit
    def validUpto: Option[LocalDate]
    def isValidAt(d: LocalDate): Boolean =
      DateUtil.isValidAt(d, validFrom, validUpto)

  object Item:
    def apply[T](gen: Int, data: T)(using
        periodProvider: EffectivePeriodProvider[T],
        repProvider: RepProvider[T],
        modelSymbol: ModelSymbol[T],
        dataId: DataId[T],
        eventFetcher: EventFetcher
    ): Item =
      ItemImpl(SyncedDataSource(gen, data))
    given Ordering[Item] = Ordering.by((item: Item) => item.validFrom).reverse
    given Comp[Item] = _.ele
    given DeleteNotifier[Item] with
      def subscribe(item: Item, handler: () => Unit) =
        item.onDelete(handler)

  class ItemImpl[T](ds: SyncedDataSource[T])(using
      periodProvider: EffectivePeriodProvider[T],
      repProvider: RepProvider[T],
      modelSymbol: ModelSymbol[T],
      dataId: DataId[T],
      eventFetcher: EventFetcher
  ) extends Item:
    def gen: Int = ds.gen
    def data: T = ds.data
    def validFrom = periodProvider.getValidFrom(data)
    def id = HokenId(data)
    def onDelete(handler: () => Unit): Unit = ds.onDelete(_ => handler())
    def validUpto = periodProvider.getValidUpto(data).value
    val ui = new ItemUI
    updateUI()
    ui.icon(onclick := (() => {
      CustomEvents.addHokenSubblock[T].trigger(ele, (gen, data))
    }))
    ds.onDelete(_ => ele.remove())
    ds.onUpdate(_ => updateUI())
    ds.startSync(ele)
    def ele = ui.ele
    def updateUI(): Unit = ui.label.innerText =
      makeLabel(repProvider.rep(data), validFrom, validUpto)

  class ItemUI:
    val icon = Icons.zoomIn()
    val label = span
    val ele =
      div(
        icon(
          Icons.defaultStyle,
          cls := "zoom-in-icon"
        ),
        label
      )

  def makeLabel(
      rep: String,
      validFrom: LocalDate,
      validUpto: Option[LocalDate]
  ): String =
    val from = KanjiDate.dateToKanji(validFrom) + "から"
    val upto = validUpto match {
      case Some(d) => KanjiDate.dateToKanji(d) + "まで"
      case None    => ""
    }
    rep + s"（${from}${upto}）"

  // def shahokokuhoRep(shahokokuho: Shahokokuho): String =
  //   HokenRep.shahokokuhoRep(
  //     shahokokuho.hokenshaBangou,
  //     shahokokuho.koureiFutanWari
  //   )

  // def roujinRep(roujin: Roujin): String = HokenRep.roujinRep(roujin.futanWari)

  // def koukikoureiRep(koukikourei: Koukikourei): String =
  //   HokenRep.koukikoureiRep(koukikourei.futanWari)

  // def kouhiRep(kouhi: Kouhi): String = HokenRep.kouhiRep(kouhi.futansha)

  // abstract class ItemBase[T](ds: SyncedDataSource[T])(using
  //     fetcher: EventFetcher,
  //     dataId: DataId[T],
  //     modelSymbol: ModelSymbol[T]
  // ) extends Item:
  //   def validFrom: LocalDate
  //   def validUpto: Option[LocalDate]
  //   def rep: String
  //   def currentData: T = ds.data
  //   def currentGen: Int = ds.gen
  //   def id = HokenId(currentData)
  //   def onDelete(handler: () => Unit): Unit =
  //     ds.onDelete(handler)

  //   val ui = new ItemUI
  //   updateUI()
  //   ds.onDelete(() => ele.remove())
  //   ds.onUpdate(() => updateUI())
  //   ds.startSync(ele)
  //   def ele = ui.ele
  //   def updateUI(): Unit = ui.label.innerText =
  //     makeLabel(rep, validFrom, validUpto)

  // class ShahokokuhoItem(ds: SyncedDataSource[Shahokokuho])(using
  //     EventFetcher
  // ) extends ItemBase[Shahokokuho](ds):
  //   def validFrom = currentData.validFrom
  //   def validUpto = currentData.validUptoOption
  //   def rep = shahokokuhoRep(currentData)

  //   ui.icon(onclick := (() => {
  //     CustomEvents.addShahokokuhoSubblock
  //       .trigger(ele, (currentGen, currentData))
  //   }))

  // class KoukikoureiItem(ds: SyncedDataSource[Koukikourei])(using
  //     EventFetcher,
  //     DataId[Koukikourei],
  //     ModelSymbol[Koukikourei]
  // ) extends ItemBase[Koukikourei](ds):
  //   def validFrom = currentData.validFrom
  //   def validUpto = currentData.validUptoOption
  //   def rep = koukikoureiRep(currentData)

  //   ui.icon(onclick := (() => {
  //     CustomEvents.addKoukikoureiSubblock
  //       .trigger(ele, (currentGen, currentData))
  //   }))

  // class RoujinItem(ds: SyncedDataSource[Roujin])(using
  //     EventFetcher,
  //     DataId[Koukikourei],
  //     ModelSymbol[Koukikourei]
  // ) extends ItemBase[Roujin](ds):
  //   def validFrom = currentData.validFrom
  //   def validUpto = currentData.validUptoOption
  //   def rep = roujinRep(currentData)

  //   ui.icon(onclick := (() => {
  //     CustomEvents.addRoujinSubblock.trigger(ele, (currentGen, currentData))
  //   }))

  // class KouhiItem(ds: SyncedDataSource[Kouhi])(using
  //     EventFetcher,
  //     DataId[Koukikourei],
  //     ModelSymbol[Koukikourei]
  // ) extends ItemBase[Kouhi](ds):
  //   def validFrom = currentData.validFrom
  //   def validUpto = currentData.validUptoOption
  //   def rep = kouhiRep(currentData)

  //   ui.icon(onclick := (() => {
  //     CustomEvents.addKouhiSubblock.trigger(ele, (currentGen, currentData))
  //   }))
