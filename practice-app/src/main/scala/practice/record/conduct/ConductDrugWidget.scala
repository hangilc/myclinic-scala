package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.practiceapp.practice.record.shinryou.RequestHelper
import cats.data.EitherT
import cats.syntax.all.*
import java.time.LocalDate
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.CreateConductRequest
import dev.myclinic.scala.model.ConductKind
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import dev.myclinic.scala.model.IyakuhinMaster

case class ConductDrugWidget(
    at: LocalDate,
    visitId: Int,
    onDone: ConductDrugWidget => Unit
):
  val nameSpan = span
  val amountInput = input
  val unitSpan = span
  val searchForm = new SearchForm[IyakuhinMaster](
    _.name,
    text => Api.searchIyakuhinMaster(text, at)
  )
  val ele = div(
    div("注射処置入力"),
    div(
      span("薬剤名称"),
      nameSpan
    ),
    div(
      span("用量"),
      amountInput,
      unitSpan
    ),
    searchForm.ele
  )

  def close(): Unit =
    ele.remove()
