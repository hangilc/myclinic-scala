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
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._

import scala.util.{Success, Failure}
import scala.concurrent.Future
import org.scalajs.dom.Event
import dev.myclinic.scala.util.DateTimeOrdering
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.DomqUtil
import dev.myclinic.scala.web.appbase.ElementDispatcher.*
import dev.myclinic.scala.web.appbase.{EventPublishers, EventFetcher}
import dev.myclinic.scala.web.appbase.EventPublisher
import dev.myclinic.scala.web.appbase.ModelPublishers

class HokenList(
    var gen: Int,
    patientId: Int,
    var shahokokuhoList: List[Shahokokuho],
    var koukikoureiList: List[Koukikourei],
    var roujinList: List[Roujin],
    var kouhiList: List[Kouhi]
)(using EventPublishers, EventFetcher):
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
  updateHokenUI()

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

  private def setHokenList(list: List[Item]): Unit =
    val listSorted = list.sortBy(list => list.validFrom).reverse
    eDisp(clear, children := listSorted.map(_.ele))

  private def updateHokenUI(): Unit =
    val list: List[Item] =
      shahokokuhoList.map(ShahokokuhoItem(gen, _))
        ++ roujinList.map(RoujinItem(gen, _))
        ++ koukikoureiList.map(KoukikoureiItem(gen, _))
        ++ kouhiList.map(KouhiItem(gen, _))
    setHokenList(list)

object HokenList:

  trait Item:
    def ele: HTMLElement
    def validFrom: LocalDate

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

  abstract class ItemBase[T](var gen: Int, var hoken: T)(using
      val publishers: EventPublishers,
      fetcher: EventFetcher
  ) extends Item:
    def validFrom: LocalDate
    def validUpto: Option[LocalDate]
    def rep: String
    def hokenId(h: T): Int
    val filterUpdatedEvent: PartialFunction[AppModelEvent, T]
    val filterDeletedEvent: PartialFunction[AppModelEvent, T]
    def addListeners(): Unit

    val ui = new ItemUI
    def ele = ui.ele
    def updateUI(): Unit = ui.label.innerText =
      makeLabel(rep, validFrom, validUpto)
    def handleEvent(g: Int, e: AppModelEvent): Unit =
      if filterUpdatedEvent.isDefinedAt(e) then
        val updated = filterUpdatedEvent(e)
        if hokenId(updated) == hokenId(hoken) then
          hoken = updated
          updateUI()
      if filterDeletedEvent.isDefinedAt(e) then
        val deleted = filterDeletedEvent(e)
        if hokenId(deleted) == hokenId(hoken) then ele.remove()
    def getData: (Int, T) = (gen, hoken)

    updateUI()
    fetcher.catchup(gen, handleEvent _)
    addListeners()

  class ShahokokuhoItem(gen: Int, shahokokuho: Shahokokuho)(using
      EventPublishers,
      EventFetcher
  ) extends ItemBase[Shahokokuho](gen, shahokokuho):
    def validFrom = hoken.validFrom
    def validUpto = hoken.validUptoOption
    def rep = shahokokuhoRep(hoken)
    def hokenId(s: Shahokokuho): Int = s.shahokokuhoId
    val filterUpdatedEvent = { case e: ShahokokuhoUpdated =>
      e.updated
    }
    val filterDeletedEvent = { case e: ShahokokuhoDeleted =>
      e.deleted
    }

    def addListeners(): Unit =
      ele.addUpdatedWithIdListener(
        publishers.shahokokuho,
        hokenId(shahokokuho),
        handleEvent _
      )

    ui.icon(onclick := (() => {
      CustomEvents.addShahokokuhoSubblock.trigger(ele, getData)
    }))

  class KoukikoureiItem(gen: Int, koukikourei: Koukikourei)(using
      EventPublishers,
      EventFetcher
  ) extends ItemBase[Koukikourei](gen, koukikourei):
    def validFrom = hoken.validFrom
    def validUpto = hoken.validUptoOption
    def rep = koukikoureiRep(hoken)
    def hokenId(s: Koukikourei): Int = s.koukikoureiId
    val filterUpdatedEvent = { case e: KoukikoureiUpdated =>
      e.updated
    }
    val filterDeletedEvent = { case e: KoukikoureiDeleted =>
      e.deleted
    }

    def addListeners(): Unit =
      ele.addUpdatedWithIdListener(
        publishers.koukikourei,
        hokenId(koukikourei),
        handleEvent _
      )

  class RoujinItem(gen: Int, roujin: Roujin)(using
      EventPublishers,
      EventFetcher
  ) extends ItemBase[Roujin](gen, roujin):
    def validFrom = hoken.validFrom
    def validUpto = hoken.validUptoOption
    def rep = roujinRep(hoken)
    def hokenId(s: Roujin): Int = s.roujinId
    val filterUpdatedEvent = { case e: RoujinUpdated =>
      e.updated
    }
    val filterDeletedEvent = { case e: RoujinDeleted =>
      e.deleted
    }

    def addListeners(): Unit =
      ele.addUpdatedWithIdListener(
        publishers.roujin,
        hokenId(roujin),
        handleEvent _
      )

  class KouhiItem(gen: Int, kouhi: Kouhi)(using
      EventPublishers,
      EventFetcher
  ) extends ItemBase[Kouhi](gen, kouhi):
    def validFrom = hoken.validFrom
    def validUpto = hoken.validUptoOption
    def rep = kouhiRep(hoken)
    def hokenId(s: Kouhi): Int = s.kouhiId
    val filterUpdatedEvent = { case e: KouhiUpdated =>
      e.updated
    }
    val filterDeletedEvent = { case e: KouhiDeleted =>
      e.deleted
    }

    def addListeners(): Unit =
      ele.addUpdatedWithIdListener(
        publishers.kouhi,
        hokenId(kouhi),
        handleEvent _
      )

