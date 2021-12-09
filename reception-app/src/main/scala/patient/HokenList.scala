package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, CustomEvent}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.scalajs.js
import dev.myclinic.scala.util.{DateUtil, HokenRep}
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import scala.concurrent.Future
import org.scalajs.dom.raw.Event
import dev.myclinic.scala.util.DateTimeOrdering
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.DomqUtil

class HokenList(patientId: Int, subblocks: HTMLElement):
  val errorBox = ErrorBox()
  val eDisp = div()
  val eListAll: HTMLElement = checkbox()
  val ele = div(
    cls := """shahokokuho-created koukikourei-created kouhi-created
      shahokokuho-updated koukikourei-updated kouhi-updated
    """,
    oncustomevent[ShahokokuhoCreated]("shahokokuho-created") := (
      (e: CustomEvent[ShahokokuhoCreated]) => onShahokokuhoCreated(e)
    ),
    oncustomevent[KoukikoureiCreated]("koukikourei-created") := (
      (e: CustomEvent[KoukikoureiCreated]) => onKoukikoureiCreated(e)
    ),
    oncustomevent[KouhiCreated]("kouhi-created") := (
      (e: CustomEvent[KouhiCreated]) => onKouhiCreated(e)
    ),
    oncustomevent[ShahokokuhoUpdated]("shahokokuho-updated") := (
      (e: CustomEvent[ShahokokuhoUpdated]) => onShahokokuhoUpdated(e)
    ),
    oncustomevent[KoukikoureiUpdated]("koukikourei-updated") := (
      (e: CustomEvent[KoukikoureiUpdated]) => onKoukikoureiUpdated(e)
    ),
    oncustomevent[KouhiUpdated]("kouhi-updated") := (
      (e: CustomEvent[KouhiUpdated]) => onKouhiUpdated(e)
    )
  )(
    errorBox.ele,
    eDisp(cls := "hoken-list-disp"),
    div(
      eListAll(onchange := onListAllChange),
      span("過去の保険も含める  ")
    )
  )

  def init(): Unit =
    loadAvailable()

  private def onShahokokuhoCreated(
      event: CustomEvent[ShahokokuhoCreated]
  ): Unit =
    val created = event.detail.created
    if eListAll.isChecked || created.isValidAt(LocalDate.now()) then
      eDisp.qSelector(s".shahokokuho-${created.shahokokuhoId}").match {
        case Some(_) => ()
        case None => {
          val item = ShahokokuhoHokenItem(event.detail.created)
          val e = createDisp(item)
          val eles = eDisp.qSelectorAll("[data-valid-from]")
          eDisp.insertInOrderDesc(
            e,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
        }
      }

  private def onKoukikoureiCreated(
      event: CustomEvent[KoukikoureiCreated]
  ): Unit =
    val created = event.detail.created
    if eListAll.isChecked || created.isValidAt(LocalDate.now()) then
      eDisp.qSelector(s".koukikourei-${created.koukikoureiId}").match {
        case Some(_) => ()
        case None => {
          val item = KoukikoureiHokenItem(event.detail.created)
          val e = createDisp(item)
          eDisp.insertInOrderDesc(
            e,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
        }
      }

  private def onKouhiCreated(
      event: CustomEvent[KouhiCreated]
  ): Unit =
    val created = event.detail.created
    if eListAll.isChecked || created.isValidAt(LocalDate.now()) then
      eDisp.qSelector(s".kouhi-${created.kouhiId}").match {
        case Some(_) => ()
        case None => {
          val item = KouhiHokenItem(event.detail.created)
          val e = createDisp(item)
          eDisp.insertInOrderDesc(
            e,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
        }
      }

  private def onShahokokuhoUpdated(
      event: CustomEvent[ShahokokuhoUpdated]
  ): Unit =
    val updated = event.detail.updated
    val cur = eDisp.qSelector(s".shahokokuho-${updated.shahokokuhoId}")
    println(("isValidAt", updated.isValidAt(LocalDate.now())))
    if eListAll.isChecked || updated.isValidAt(LocalDate.now()) then
      val item = HokenItem(updated)
      val newEle = createDisp(item)
      cur match {
        case Some(c) => c.replaceBy(newEle)
        case None =>
          eDisp.insertInOrderDesc(
            newEle,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
      }
    else
      cur match {
        case Some(c) => c.remove()
        case None => ()
      }

  private def onKoukikoureiUpdated(
      event: CustomEvent[KoukikoureiUpdated]
  ): Unit =
    val updated = event.detail.updated
    val cur = eDisp.qSelector(s".koukikourei-${updated.koukikoureiId}")
    if eListAll.isChecked || updated.isValidAt(LocalDate.now()) then
      val item = HokenItem(updated)
      val newEle = createDisp(item)
      cur match {
        case Some(c) => c.replaceBy(newEle)
        case None =>
          eDisp.insertInOrderDesc(
            newEle,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
      }
    else
      cur match {
        case Some(c) => c.remove()
        case None => ()
      }

  private def onKouhiUpdated(
      event: CustomEvent[KouhiUpdated]
  ): Unit =
    val updated = event.detail.updated
    val cur = eDisp.qSelector(s".kouhi-${updated.kouhiId}")
    if eListAll.isChecked || updated.isValidAt(LocalDate.now()) then
      val item = HokenItem(updated)
      val newEle = createDisp(item)
      cur match {
        case Some(c) => c.replaceBy(newEle)
        case None =>
          eDisp.insertInOrderDesc(
            newEle,
            "[data-valid-from]",
            _.getAttribute("data-valid-from")
          )
      }
    else
      cur match {
        case Some(c) => c.remove()
        case None => ()
      }

  private def setHokenList(list: List[HokenItem]): Unit =
    val listSorted = list.sortBy(list => list.validFrom).reverse
    eDisp.clear()
    eDisp((listSorted.map(createDisp(_)): List[Modifier]): _*)

  private def createDisp(item: HokenItem): HTMLElement =
    div(
      attr("data-valid-from") := DateUtil.toSqlDate(item.validFrom),
      cls := item.key,
      oncustomevent[ShahokokuhoDeleted]("shahokokuho-deleted") := (
        (e: CustomEvent[ShahokokuhoDeleted]) => {
          e.target.asInstanceOf[HTMLElement].remove()
        }
      ),
      oncustomevent[KoukikoureiDeleted]("koukikourei-deleted") := (
        (e: CustomEvent[KoukikoureiDeleted]) => {
          e.target.asInstanceOf[HTMLElement].remove()
        }
      ),
      oncustomevent[KouhiDeleted]("kouhi-deleted") := (
        (e: CustomEvent[KouhiDeleted]) => {
          e.target.asInstanceOf[HTMLElement].remove()
        }
      )
    )(
      Icons.zoomIn(color = "gray", size = "1.2rem")(
        Icons.defaultStyle,
        cls := "zoom-in-icon",
        onclick := (() => {
          subblocks.prepend(createSubblock(item).ele)
        })
      ),
      item.repFull
    )

  private def createSubblock(item: HokenItem): Subblock =
    val sub = item.createSubblock()
    sub.ele(cls := item.key)
    sub

  private def loadAvailable(): Unit =
    val date = LocalDate.now()
    (for
      shahoList <- Api.listAvailableShahokokuho(patientId, date)
      roujinList <- Api.listAvailableRoujin(patientId, date)
      koukikoureiList <- Api.listAvailableKoukikourei(patientId, date)
      kouhiList <- Api.listAvailableKouhi(patientId, date)
    yield {
      val list = shahoList.map(ShahokokuhoHokenItem(_))
        ++ roujinList.map(RoujinHokenItem(_))
        ++ koukikoureiList.map(KoukikoureiHokenItem(_))
        ++ kouhiList.map(KouhiHokenItem(_))
      setHokenList(list)
    }).onComplete {
      case Success(_)  => ()
      case Failure(ex) => errorBox.show(ex.getMessage)
    }

  private def loadAll(): Unit =
    (for
      shahoList <- Api.listShahokokuho(patientId)
      roujinList <- Api.listRoujin(patientId)
      koukikoureiList <- Api.listKoukikourei(patientId)
      kouhiList <- Api.listKouhi(patientId)
    yield {
      val list = shahoList.map(ShahokokuhoHokenItem(_))
        ++ roujinList.map(RoujinHokenItem(_))
        ++ koukikoureiList.map(KoukikoureiHokenItem(_))
        ++ kouhiList.map(KouhiHokenItem(_))
      setHokenList(list)
    }).onComplete {
      case Success(_)  => ()
      case Failure(ex) => errorBox.show(ex.getMessage)
    }

  private lazy val onListAllChange: js.Function1[Event, Unit] =
    (event: Event) => {
      if eListAll.isChecked then loadAll()
      else loadAvailable()
    }

sealed trait HokenItem:
  def rep: String
  def validFrom: LocalDate
  def validUpto: Option[LocalDate]
  def key: String
  def createSubblock(): Subblock
  def repFull: String =
    val from = KanjiDate.dateToKanji(validFrom) + "から"
    val upto = validUpto match {
      case Some(d) => KanjiDate.dateToKanji(d) + "まで"
      case None    => ""
    }
    rep + s"（${from}${upto}）"

object HokenItem:
  def apply(src: Shahokokuho | Roujin | Koukikourei | Kouhi): HokenItem =
    src match {
      case h: Shahokokuho => ShahokokuhoHokenItem(h)
      case h: Roujin      => RoujinHokenItem(h)
      case h: Koukikourei => KoukikoureiHokenItem(h)
      case h: Kouhi       => KouhiHokenItem(h)
    }

class ShahokokuhoHokenItem(shahokokuho: Shahokokuho) extends HokenItem:
  def rep: String = HokenRep.shahokokuhoRep(
    shahokokuho.hokenshaBangou,
    shahokokuho.koureiFutanWari
  )
  def validFrom: LocalDate = shahokokuho.validFrom
  def validUpto: Option[LocalDate] = shahokokuho.validUptoOption
  def key: String = s"shahokokuho-${shahokokuho.shahokokuhoId}"
  def createSubblock(): Subblock = ShahokokuhoSubblock(shahokokuho).block

class RoujinHokenItem(roujin: Roujin) extends HokenItem:
  def rep: String = HokenRep.roujinRep(roujin.futanWari)
  def validFrom: LocalDate = roujin.validFrom
  def validUpto: Option[LocalDate] = roujin.validUptoOption
  def key: String = s"roujin-${roujin.roujinId}"
  def createSubblock(): Subblock = Subblock(
    "老人保険",
    div(),
    div()
  )

class KoukikoureiHokenItem(koukikourei: Koukikourei) extends HokenItem:
  def rep: String = HokenRep.koukikoureiRep(koukikourei.futanWari)
  def validFrom: LocalDate = koukikourei.validFrom
  def validUpto: Option[LocalDate] = koukikourei.validUptoOption
  def key: String = s"koukikourei-${koukikourei.koukikoureiId}"
  def createSubblock(): Subblock = KoukikoureiSubblock(koukikourei).block

class KouhiHokenItem(kouhi: Kouhi) extends HokenItem:
  def rep: String = HokenRep.kouhiRep(kouhi.futansha)
  def validFrom: LocalDate = kouhi.validFrom
  def validUpto: Option[LocalDate] = kouhi.validUptoOption
  def key: String = s"kouhi-${kouhi.kouhiId}"
  def createSubblock(): Subblock = Subblock(
    "公費",
    div(),
    div()
  )
