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

case class XpWidget(at: LocalDate, visitId: Int, onDone: XpWidget => Unit):
  val labelSelect = select
  val filmSelect = select
  val ele = div(
    div("Ｘ線検査入力"),
    div(
      labelSelect(
        option("胸部単純Ｘ線"),
        option("腹部単純Ｘ線")
      ),
      filmSelect(
        option("大角"),
        option("四ツ切")
      )
    ),
    div(
      button("入力", onclick := (() => doEnter)),
      button("キャンセル", onclick := (close _))
    )
  )

  def close(): Unit =
    ele.remove()

  def doEnter: Unit =
    val label = labelSelect.value
    val film = filmSelect.value
    val op = for
      shinryou1 <- EitherT(RequestHelper.conductShinryouReq("単純撮影", at))
      shinryou2 <- EitherT(RequestHelper.conductShinryouReq("単純撮影診断", at))
      kizai <- EitherT(RequestHelper.conductKizaiReq(film, 1.0, at))
      creq = CreateConductRequest(visitId, ConductKind.Gazou.code, Some(label),
        shinryouList = List(shinryou1, shinryou2),
        kizaiList = List(kizai))
      enterResult <- EitherT.right(RequestHelper.batchEnter(conductList = List(creq)))
      (_, conductIds) = enterResult
      conductEx <- EitherT.right(Api.getConductEx(conductIds(0)))
    yield PracticeBus.conductEntered.publish(conductEx)
    for
      result <- op.value
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(_) => onDone(this)
    }