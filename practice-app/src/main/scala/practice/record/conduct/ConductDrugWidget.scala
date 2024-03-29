package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.practiceapp.practice.record.CodeResolver
import dev.myclinic.scala.web.practiceapp.practice.record.CreateHelper
import cats.data.EitherT
import cats.syntax.all.*
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.CreateConductRequest
import dev.myclinic.scala.model.ConductKind
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.model.IyakuhinMaster
import scala.util.Try
import scala.concurrent.Future
import dev.myclinic.scala.model.ConductShinryou
import dev.myclinic.scala.model.ConductDrug
import scala.language.implicitConversions

case class ConductDrugWidget(
    at: LocalDate,
    visitId: Int,
    onDone: ConductDrugWidget => Unit
):
  private var master: Option[IyakuhinMaster] = None
  val cssPre = "practice-enter-conduct-drug-widget"
  val nameSpan = span
  val amountInput = input
  val unitSpan = span
  val kindGroup = RadioGroup[ConductKind](
    List(
      "皮下・筋肉" -> ConductKind.HikaChuusha,
      "静注" -> ConductKind.JoumyakuChuusha,
      "その他" -> ConductKind.OtherChuusha
    )
  )
  val searchForm = new SearchForm[IyakuhinMaster](
    master => div(innerText := master.name),
    text => Api.searchIyakuhinMaster(text, at)
  )
  searchForm.onSelect(m =>
    master = Some(m)
    nameSpan(innerText := m.name)
    unitSpan(innerText := m.unit)
  )
  val ele = div(cls := s"practice-widget ${cssPre}",
    div(cls := "practice-widget-title", "注射処置入力"),
    div(
      span("薬剤名称"),
      nameSpan(cls := s"${cssPre}-name-label-span")
    ),
    div(cls := s"$cssPre-amount",
      span("用量"),
      amountInput(cls := s"$cssPre-amount-input"),
      unitSpan(cls := s"$cssPre-amount-unit")
    ),
    kindGroup.ele(cls := s"$cssPre-kind-group"),
    div(cls := "practice-widget-commands",
      button("入力", onclick := (doEnter _)),
      button("キャンセル", onclick := (close _))
    ),
    searchForm.ele(cls := s"$cssPre-search")
  )

  def close(): Unit =
    ele.remove()

  def doEnter(): Unit =
    val op = for
      master <- EitherT.fromEither[Future](getMaster)
      amount <- EitherT.fromEither[Future](getAmount)
      kind = getKind
      conductDrug = ConductDrug(0, 0, master.iyakuhincode, amount)
      shinryouOption <- getShinryou
      createConductReq = CreateConductRequest(
        visitId,
        kind.code,
        None,
        shinryouList = shinryouOption.toList,
        drugs = List(conductDrug)
      )
      conductEx <- EitherT.right(
        CreateHelper.enterConduct(createConductReq)
      )
    yield PracticeBus.conductEntered.publish(conductEx)
    for
      result <- op.value
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(_) => ele.remove()
    }

  def getMaster: Either[String, IyakuhinMaster] =
    Either.fromOption(master, "医薬品が選択されていません。")

  def getAmount: Either[String, Double] =
    Either.fromOption(amountInput.value.toDoubleOption, "用量の入力が不適切です。")

  def getKind: ConductKind = kindGroup.selected

  def getShinryou: EitherT[Future, String, Option[ConductShinryou]] =
    getKind match {
      case ConductKind.HikaChuusha =>
        for
          cs <- CreateHelper.conductShinryouReqByName("皮下筋注", at)
        yield Some(cs)
      case ConductKind.JoumyakuChuusha =>
        for
          cs <- CreateHelper.conductShinryouReqByName("静注", at)
        yield Some(cs)
      case _ => EitherT.rightT[Future, String](None)
    }
