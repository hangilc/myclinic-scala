package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, CustomEvent}
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
import dev.myclinic.scala.web.appbase.{DataSource, SyncedDataSource}
import dev.myclinic.scala.web.appbase.ListOfSortedComp
import dev.myclinic.scala.web.appbase.Comp
import dev.myclinic.scala.web.appbase.LocalEventPublisher
import dev.myclinic.scala.web.appbase.DeleteNotifier
import dev.myclinic.scala.web.appbase.ElementEvent.*

class HokenList(
    var gen: Int,
    patientId: Int,
    var shahokokuhoList: List[Shahokokuho],
    var koukikoureiList: List[Koukikourei],
    var roujinList: List[Roujin],
    var kouhiList: List[Kouhi]
)(using EventFetcher):
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
  val list: ListOfSortedComp[Item] = ListOfSortedComp(eDisp)
  updateHokenUI()
  ele.addCreatedListener[Shahokokuho](event => {
    val gen = event.appEventId
    val created = event.dataAs[Shahokokuho]
    val item = ShahokokuhoItem(SyncedDataSource(gen, created))
    list.insert(item)
  })

  private def onListAllChange(): Unit =
    (for
      (g, _, s, kk, r, kh) <-
        if eListAll.checked then Api.getPatientAllHoken(patientId)
        else Api.getPatientHoken(patientId, LocalDate.now())
    yield
      gen = g
      shahokokuhoList = s
      koukikoureiList = kk
      roujinList = r
      kouhiList = kh
      updateHokenUI()
    ).onComplete {
      case Success(_)  => ()
      case Failure(ex) => System.err.println(ex.getMessage)
    }

  private def setHokenList(items: List[Item]): Unit = list.set(items)

  private def updateHokenUI(): Unit =
    val list: List[Item] =
      shahokokuhoList.map(shaho => ShahokokuhoItem(SyncedDataSource(gen, shaho)))
        ++ roujinList.map(roujin => RoujinItem(SyncedDataSource(gen, roujin)))
        ++ koukikoureiList.map(koukikourei =>
          KoukikoureiItem(SyncedDataSource(gen, koukikourei))
        )
        ++ kouhiList.map(kouhi => KouhiItem(SyncedDataSource(gen, kouhi)))
    setHokenList(list)

object HokenList:

  trait Item:
    def ele: HTMLElement
    def validFrom: LocalDate
    def id: (String, Int)
    def onDelete(handler: () => Unit): Unit

  object Item:
    given Ordering[Item] = Ordering.by((item: Item) => item.validFrom).reverse
    given Comp[Item] = _.ele
    given DeleteNotifier[Item] with
      def subscribe(item: Item, handler: () => Unit) =
        item.onDelete(handler)

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

  def shahokokuhoRep(shahokokuho: Shahokokuho): String =
    HokenRep.shahokokuhoRep(
      shahokokuho.hokenshaBangou,
      shahokokuho.koureiFutanWari
    )

  def roujinRep(roujin: Roujin): String = HokenRep.roujinRep(roujin.futanWari)

  def koukikoureiRep(koukikourei: Koukikourei): String =
    HokenRep.koukikoureiRep(koukikourei.futanWari)

  def kouhiRep(kouhi: Kouhi): String = HokenRep.kouhiRep(kouhi.futansha)

  abstract class ItemBase[T](ds: SyncedDataSource[T])(using
      fetcher: EventFetcher,
      dataId: DataId[T],
      modelSymbol: ModelSymbol[T]
  ) extends Item:
    def validFrom: LocalDate
    def validUpto: Option[LocalDate]
    def rep: String
    def currentData: T = ds.data
    def currentGen: Int = ds.gen
    def id: (String, Int) = (modelSymbol.getSymbol, dataId.getId(currentData))
    def onDelete(handler: () => Unit): Unit =
      ds.onDelete(handler)

    val ui = new ItemUI
    updateUI()
    ds.onDelete(() => ele.remove())
    ds.startSync(ele)
    def ele = ui.ele
    def updateUI(): Unit = ui.label.innerText =
      makeLabel(rep, validFrom, validUpto)

  class ShahokokuhoItem(ds: SyncedDataSource[Shahokokuho])(using
      EventFetcher
  ) extends ItemBase[Shahokokuho](ds):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = shahokokuhoRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addShahokokuhoSubblock
        .trigger(ele, (currentGen, currentData))
    }))

  class KoukikoureiItem(ds: SyncedDataSource[Koukikourei])(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Koukikourei](ds):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = koukikoureiRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addKoukikoureiSubblock
        .trigger(ele, (currentGen, currentData))
    }))

  class RoujinItem(ds: SyncedDataSource[Roujin])(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Roujin](ds):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = roujinRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addRoujinSubblock.trigger(ele, (currentGen, currentData))
    }))

  class KouhiItem(ds: SyncedDataSource[Kouhi])(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Kouhi](ds):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = kouhiRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addKouhiSubblock.trigger(ele, (currentGen, currentData))
    }))
