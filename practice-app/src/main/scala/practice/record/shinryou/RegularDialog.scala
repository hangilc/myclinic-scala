package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import dev.myclinic.scala.model.ShinryouEx
import dev.myclinic.scala.model.CreateConductRequest
import dev.myclinic.scala.model.ConductKind
import dev.myclinic.scala.model.ConductShinryou
import dev.myclinic.scala.model.Shinryou
import scala.concurrent.Future
import dev.myclinic.scala.model.ConductKizai
import cats.*
import cats.implicits.*
import cats.data.EitherT
import dev.myclinic.scala.model.CreateShinryouConductRequest

class RegularDialog(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String],
    at: LocalDate,
    visitId: Int
):
  val panel = RegularPanel(leftNames, rightNames, bottomNames)
  val dlog = new ModalDialog3
  dlog.title("診療行為入力")
  dlog.body(panel.ele)
  dlog.commands(
    button("入力", onclick := (onEnter _)),
    button("キャンセル", onclick := (_ => dlog.close()))
  )
  def open: Unit =
    dlog.open()

  def onEnter(): Unit =
    val (names, kotsuen): (List[String], Boolean) =
      panel.selected.foldLeft[(List[String], Boolean)]((List.empty, false)) {
        case ((n, k), name) =>
          name match {
            case "骨塩定量" => (n, true)
            case _      => (n :+ name, k)
          }
      }
    val op = (for
      shinryouList <- EitherT[Future, String, List[Shinryou]](
        names
          .map(name => RequestHelper.shinryou(name, at, visitId))
          .sequence
          .map(_.sequence)
      )
      conductOption <-
        if kotsuen then EitherT(kotsuenReq).map(Some(_))
        else EitherT.rightT[Future, String](None)
      req = CreateShinryouConductRequest(shinryouList, conductOption.toList)
      enterResult <- EitherT.right(Api.batchEnterShinryouConduct(req))
      (shinryouIds, conductIds) = enterResult
    yield (shinryouIds, conductIds)).value
    for result <- op
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(shinryouIds, conductIds) =>
        println(("entered", shinryouIds, conductIds))
    }

  def kotsuenReq: Future[Either[String, CreateConductRequest]] =
    (for
      shinryou <- EitherT(
        RequestHelper.conductShinryouReq("骨塩定量ＭＤ法", at)
      )
      kizai <- EitherT(
        RequestHelper.conductKizaiReq("四ツ切", 1, at)
      )
    yield CreateConductRequest(
      visitId,
      ConductKind.Gazou.code,
      Some("骨塩定量に使用"),
      shinryouList = List(shinryou),
      kizaiList = List(kizai)
    )).value
