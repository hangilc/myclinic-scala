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
import dev.myclinic.scala.util.DateTimeOrdering
import scala.math.Ordered.orderingToOrdered
import dev.fujiwara.domq.DomqUtil
import dev.myclinic.scala.web.appbase.{EventFetcher}
import dev.myclinic.scala.web.appbase.SyncedComp

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

  abstract class ItemBase[T](gen: Int, hoken: T)(using
      EventFetcher,
      DataId[T],
      ModelSymbol[T]
  ) extends SyncedComp[T](gen, hoken)
      with Item:
    def validFrom: LocalDate
    def validUpto: Option[LocalDate]
    def rep: String

    val ui = new ItemUI
    def ele = ui.ele
    def updateUI(): Unit = ui.label.innerText =
      makeLabel(rep, validFrom, validUpto)

    initSyncedComp()

  class ShahokokuhoItem(_gen: Int, _shahokokuho: Shahokokuho)(using
      EventFetcher
  ) extends ItemBase[Shahokokuho](_gen, _shahokokuho):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = shahokokuhoRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addShahokokuhoSubblock
        .trigger(ele, (currentGen, currentData))
    }))

  class KoukikoureiItem(_gen: Int, _koukikourei: Koukikourei)(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Koukikourei](_gen, _koukikourei):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = koukikoureiRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addKoukikoureiSubblock
        .trigger(ele, (currentGen, currentData))
    }))

  class RoujinItem(_gen: Int, _roujin: Roujin)(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Roujin](_gen, _roujin):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = roujinRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addRoujinSubblock.trigger(ele, (currentGen, currentData))
    }))

  class KouhiItem(_gen: Int, _kouhi: Kouhi)(using
      EventFetcher,
      DataId[Koukikourei],
      ModelSymbol[Koukikourei]
  ) extends ItemBase[Kouhi](_gen, _kouhi):
    def validFrom = currentData.validFrom
    def validUpto = currentData.validUptoOption
    def rep = kouhiRep(currentData)

    ui.icon(onclick := (() => {
      CustomEvents.addKouhiSubblock.trigger(ele, (currentGen, currentData))
    }))
