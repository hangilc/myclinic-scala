package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier, CustomEvent}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import scala.scalajs.js
import dev.myclinic.scala.util.{KanjiDate, DateUtil, HokenRep}
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
    cls := "shahokokuho-created",
    oncustomevent[ShahokokuhoCreated]("shahokokuho-created") := (
      (e: CustomEvent[ShahokokuhoCreated]) => onShahokokuhoCreated(e)
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
    eDisp.qSelector(s".shahokokuho-${created.shahokokuhoId}").match {
      case Some(_) => ()
      case None => {
        val item = ShahokokuhoHokenItem(event.detail.created)
        val e = createDisp(item)
        val eles = eDisp.qSelectorAll("[data-valid-from]")
        DomqUtil.insertInOrderDesc(e, eles, _.getAttribute("data-valid-from"))
      }
    }

  private def setHokenList(list: List[HokenItem]): Unit =
    val listSorted = list.sortBy(list => list.validFrom).reverse
    eDisp.clear()
    eDisp((listSorted.map(createDisp(_)): List[Modifier]): _*)

  private def createDisp(item: HokenItem): HTMLElement =
    div(
      attr("data-valid-from") := DateUtil.toSqlDate(item.validFrom),
      cls := item.key,
      oncustomevent[ShahokokuhoDeleted]("shahokokuho-deleted") := ((e: CustomEvent[ShahokokuhoDeleted]) => {
        e.target.asInstanceOf[HTMLElement].remove()
        ()
      })
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
    val sub = item.createDisp()
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
  def createDisp(): Subblock
  def repFull: String =
    val from = KanjiDate.dateToKanji(validFrom) + "から"
    val upto = validUpto match {
      case Some(d) => KanjiDate.dateToKanji(d) + "まで"
      case None    => ""
    }
    rep + s"（${from}${upto}）"

class ShahokokuhoHokenItem(shahokokuho: Shahokokuho) extends HokenItem:
  def rep: String = HokenRep.shahokokuhoRep(
    shahokokuho.hokenshaBangou,
    shahokokuho.koureiFutanWari
  )
  def validFrom: LocalDate = shahokokuho.validFrom
  def validUpto: Option[LocalDate] = shahokokuho.validUptoOption
  def key: String = s"shahokokuho-${shahokokuho.shahokokuhoId}"
  def createDisp(): Subblock = ShahokokuhoSubblock(shahokokuho).block

class RoujinHokenItem(roujin: Roujin) extends HokenItem:
  def rep: String = HokenRep.roujinRep(roujin.futanWari)
  def validFrom: LocalDate = roujin.validFrom
  def validUpto: Option[LocalDate] = roujin.validUptoOption
  def key: String = s"roujin-${roujin.roujinId}"
  def createDisp(): Subblock = Subblock(
    "老人保険",
    div(),
    div()
  )

class KoukikoureiHokenItem(koukikourei: Koukikourei) extends HokenItem:
  def rep: String = HokenRep.koukikoureiRep(koukikourei.futanWari)
  def validFrom: LocalDate = koukikourei.validFrom
  def validUpto: Option[LocalDate] = koukikourei.validUptoOption
  def key: String = s"koukikourei-${koukikourei.koukikoureiId}"
  def createDisp(): Subblock = Subblock(
    "後期高齢",
    div(),
    div()
  )

class KouhiHokenItem(kouhi: Kouhi) extends HokenItem:
  def rep: String = HokenRep.kouhiRep(kouhi.futansha)
  def validFrom: LocalDate = kouhi.validFrom
  def validUpto: Option[LocalDate] = kouhi.validUptoOption
  def key: String = s"kouhi-${kouhi.kouhiId}"
  def createDisp(): Subblock = Subblock(
    "公費",
    div(),
    div()
  )
