package dev.myclinic.scala.web.practiceapp.practice.record.hoken

import dev.myclinic.scala.model.{HokenInfo, HokenIdSet}
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.apputil.HokenUtil.Ext.*
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import scala.language.implicitConversions

class EditDialog(
    shahoOpt: Option[Shahokokuho],
    koukikoureiOpt: Option[Koukikourei],
    roujinOpt: Option[Roujin],
    kouhiList: List[Kouhi],
    current: HokenInfo,
    visitId: Int
):
  val shahokokuhoCheck: Option[CheckLabel[Shahokokuho]] =
    shahoOpt.map(h => CheckLabel[Shahokokuho](h, h.rep))
  val koukikoureiCheck: Option[CheckLabel[Koukikourei]] =
    koukikoureiOpt.map(h => CheckLabel[Koukikourei](h, h.rep))
  val roujinCheck: Option[CheckLabel[Roujin]] =
    roujinOpt.map(h => CheckLabel[Roujin](h, h.rep))
  val kouhiChecks: List[CheckLabel[Kouhi]] =
    kouhiList.map(h => CheckLabel[Kouhi](h, h.rep))

  for
    c <- shahokokuhoCheck
    h <- current.shahokokuho
  yield if h.shahokokuhoId == c.value.shahokokuhoId then c.check

  for
    c <- koukikoureiCheck
    h <- current.koukikourei
  yield if h.koukikoureiId == c.value.koukikoureiId then c.check

  for
    c <- roujinCheck
    h <- current.roujin
  yield if h.roujinId == c.value.roujinId then c.check

  kouhiChecks.foreach(c =>
    val kouhiId = c.value.kouhiId
    if current.kouhiList.find(k => k.kouhiId == kouhiId).isDefined then c.check
  )

  val dlog = new ModalDialog3()
  dlog.title(innerText := "保険選択")
  dlog.body(
    eleOf(shahokokuhoCheck),
    eleOf(koukikoureiCheck),
    eleOf(roujinCheck),
    kouhiChecks.map(_.wrap(div))
  )
  dlog.commands(
    button("入力", onclick := (onEnter _)),
    button("キャンセル", onclick := (_ => dlog.close()))
  )
  
  def open: Unit = dlog.open()

  def eleOf[T](c: Option[CheckLabel[T]]): Option[HTMLElement] =
    c.map(c => c.wrap(div))

  def onEnter(): Unit =
    import dev.myclinic.scala.util.FunUtil.*
    val idSet = HokenIdSet(
      shahokokuhoId = shahokokuhoCheck.flatMap(c => c.selected).map(h => h.shahokokuhoId).getOrElse(0),
      koukikoureiId = koukikoureiCheck.flatMap(c => c.selected).map(h => h.koukikoureiId).getOrElse(0),
      roujinId = roujinCheck.flatMap(c => c.selected).map(h => h.roujinId).getOrElse(0),
      kouhi1Id = kouhiChecks.applyOption(0).flatMap(c => c.selected).map(h => h.kouhiId).getOrElse(0),
      kouhi2Id = kouhiChecks.applyOption(1).flatMap(c => c.selected).map(h => h.kouhiId).getOrElse(0),
      kouhi3Id = kouhiChecks.applyOption(2).flatMap(c => c.selected).map(h => h.kouhiId).getOrElse(0),
    )
    for
      _ <- Api.updateHokenIds(visitId, idSet)
      newHoken <- Api.getHokenInfo(visitId)
    yield 
      PracticeBus.hokenInfoChanged.publish(visitId, newHoken)
      dlog.close()
    
