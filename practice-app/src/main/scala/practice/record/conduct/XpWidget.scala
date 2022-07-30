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
import scala.language.implicitConversions

case class XpWidget(at: LocalDate, visitId: Int, onDone: XpWidget => Unit):
  val labelSelect = select
  val filmSelect = select
  val ele = div(cls := "practice-widget",
    div(cls := "practice-widget-title", "Ｘ線検査入力"),
    div(cls := "practice-conduct-enter-xp-form",
      labelSelect(
        option("胸部単純Ｘ線"),
        option("腹部単純Ｘ線")
      ),
      filmSelect(
        option("大角"),
        option("四ツ切")
      )
    ),
    div(cls := "practice-widget-commands",
      button("入力", onclick := (() => doEnter)),
      button("キャンセル", onclick := (close _))
    )
  )

  def close(): Unit =
    ele.remove()

  def doEnter: Unit =
    val label = labelSelect.value
    val film = filmSelect.value
    val op = 
      for
        shinryou1 <- CreateHelper.conductShinryouReqByName("単純撮影", at)
        shinryou2 <- CreateHelper.conductShinryouReqByName("単純撮影診断", at)
        kizai <- CreateHelper.conductKizaiReqByName(film, 1.0, at)
        creq = CreateConductRequest(visitId, ConductKind.Gazou.code, Some(label),
          shinryouList = List(shinryou1, shinryou2),
          kizaiList = List(kizai))
        conductEx <- EitherT.right(CreateHelper.enterConduct(creq))
      yield PracticeBus.conductEntered.publish(conductEx)
    for
      result <- op.value
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(_) => onDone(this)
    }