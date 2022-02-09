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
import dev.myclinic.scala.web.appbase.EventPublishers
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.myclinic.scala.web.appbase.ElementDispatcher.*

class HokenList(
    var gen: Int,
    patientId: Int,
    var shahokokuhoList: List[Shahokokuho],
    var koukikoureiList: List[Koukikourei],
    var roujinList: List[Roujin],
    var kouhiList: List[Kouhi]
)(using EventPublishers, EventFetcher):
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

  private def setHokenList(list: List[HokenItem]): Unit =
    val listSorted = list.sortBy(list => list.validFrom).reverse
    eDisp.clear()
    eDisp((listSorted.map(createDisp(_)): List[Modifier[HTMLElement]]): _*)

  private def createDisp(item: HokenItem): HTMLElement =
    import HokenList.*
    item match {
      case ShahokokuhoHokenItem(gen, h) =>
        ShahokokuhoDisp(
          gen,
          h,
          (gen, hoken) =>
            CustomEvents.addShahokokuhoSubblock.trigger(ele, (gen, hoken))
        ).ele
      case KoukikoureiHokenItem(gen, h) =>
        KoukikoureiDisp(gen, h, (gen, hoken) => ()).ele
      case RoujinHokenItem(gen, h) =>
        RoujinDisp(gen, h, (gen, hoken) => ()).ele
      case KouhiHokenItem(gen, h) => KouhiDisp(gen, h, (gen, hoken) => ()).ele
    }

  private def updateHokenUI(): Unit =
    val list: List[HokenItem] =
      shahokokuhoList.map(ShahokokuhoHokenItem(gen, _))
        ++ roujinList.map(RoujinHokenItem(gen, _))
        ++ koukikoureiList.map(KoukikoureiHokenItem(gen, _))
        ++ kouhiList.map(KouhiHokenItem(gen, _))
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
        span
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

  def shahokokuhoLabel(shahokokuho: Shahokokuho): String =
    makeLabel(
      shahokokuhoRep(shahokokuho),
      shahokokuho.validFrom,
      shahokokuho.validUptoOption
    )

  class ShahokokuhoItem(var gen: Int, var shahokokuho: Shahokokuho)(using
      publishers: EventPublishers,
      fetcher: EventFetcher
  ) extends Item:
    val ui = new ItemUI
    def ele = ui.ele
    def validFrom = shahokokuho.validFrom

    updateUI()
    fetcher.catchup(gen, (g, e) => {
      e match {
        case ShahokokuhoUpdated(at, updated) => 
          if updated.shahokokuhoId == shahokokuho.shahokokuhoId then
            shahokokuho = updated
            updateUI()
        case ShahokokuhoDeleted(at, deleted) =>
          if deleted.shahokokuhoId == shahokokuho.shahokokuhoId then
            ele.remove()
        case _ => ()
      }
      gen = g
    })
    ele.addUpdatedListener(publishers.shahokokuho, (g, e) => {
      
    })
    
     publishers.shahokokuho

    def updateUI(): Unit = ui.label.innerText = shahokokuhoLabel(shahokokuho)

  class DispUI:
    val icon = Icons.zoomIn()
    val ele =
      div(
        icon(
          Icons.defaultStyle,
          cls := "zoom-in-icon"
        )
      )

  abstract class Disp(ui: DispUI, item: HokenItem):
    val ele = ui.ele
    ele(
      attr("data-valid-from") := DateUtil.toSqlDate(item.validFrom)
    )(item.repFull)
    ui.icon(onclick := (onIconClick _))

    def onIconClick(): Unit

  class ShahokokuhoDisp(
      gen: Int,
      shahokokuho: Shahokokuho,
      onSelect: (Int, Shahokokuho) => Unit
  ) extends Disp(new DispUI, HokenItem(gen, shahokokuho)):
    def onIconClick(): Unit = onSelect(gen, shahokokuho)

  class KoukikoureiDisp(
      gen: Int,
      koukikourei: Koukikourei,
      onSelect: (Int, Koukikourei) => Unit
  ) extends Disp(new DispUI, HokenItem(gen, koukikourei)):
    def onIconClick(): Unit = onSelect(gen, koukikourei)

  class RoujinDisp(gen: Int, roujin: Roujin, onSelect: (Int, Roujin) => Unit)
      extends Disp(new DispUI, HokenItem(gen, roujin)):
    def onIconClick(): Unit = onSelect(gen, roujin)

  class KouhiDisp(gen: Int, kouhi: Kouhi, onSelect: (Int, Kouhi) => Unit)
      extends Disp(new DispUI, HokenItem(gen, kouhi)):
    def onIconClick(): Unit = onSelect(gen, kouhi)

sealed trait HokenItem:
  def rep: String
  def validFrom: LocalDate
  def validUpto: Option[LocalDate]
  def repFull: String =
    val from = KanjiDate.dateToKanji(validFrom) + "から"
    val upto = validUpto match {
      case Some(d) => KanjiDate.dateToKanji(d) + "まで"
      case None    => ""
    }
    rep + s"（${from}${upto}）"

object HokenItem:
  def apply(
      gen: Int,
      src: Shahokokuho | Roujin | Koukikourei | Kouhi
  ): HokenItem =
    src match {
      case h: Shahokokuho => ShahokokuhoHokenItem(gen, h)
      case h: Roujin      => RoujinHokenItem(gen, h)
      case h: Koukikourei => KoukikoureiHokenItem(gen, h)
      case h: Kouhi       => KouhiHokenItem(gen, h)
    }

case class ShahokokuhoHokenItem(gen: Int, shahokokuho: Shahokokuho)
    extends HokenItem:
  def rep: String = HokenRep.shahokokuhoRep(
    shahokokuho.hokenshaBangou,
    shahokokuho.koureiFutanWari
  )
  def validFrom: LocalDate = shahokokuho.validFrom
  def validUpto: Option[LocalDate] = shahokokuho.validUptoOption
  def key: String = s"shahokokuho-${shahokokuho.shahokokuhoId}"

case class RoujinHokenItem(gen: Int, roujin: Roujin) extends HokenItem:
  def rep: String = HokenRep.roujinRep(roujin.futanWari)
  def validFrom: LocalDate = roujin.validFrom
  def validUpto: Option[LocalDate] = roujin.validUptoOption
  def key: String = s"roujin-${roujin.roujinId}"
  def createSubblock(): Subblock = Subblock(
    "老人保険",
    div(),
    div()
  )

case class KoukikoureiHokenItem(gen: Int, koukikourei: Koukikourei)
    extends HokenItem:
  def rep: String = HokenRep.koukikoureiRep(koukikourei.futanWari)
  def validFrom: LocalDate = koukikourei.validFrom
  def validUpto: Option[LocalDate] = koukikourei.validUptoOption
  def key: String = s"koukikourei-${koukikourei.koukikoureiId}"

case class KouhiHokenItem(gen: Int, kouhi: Kouhi) extends HokenItem:
  def rep: String = HokenRep.kouhiRep(kouhi.futansha)
  def validFrom: LocalDate = kouhi.validFrom
  def validUpto: Option[LocalDate] = kouhi.validUptoOption
  def key: String = s"kouhi-${kouhi.kouhiId}"
