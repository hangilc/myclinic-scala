package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import cats.syntax.all.*
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.record.CreateHelper
import dev.myclinic.scala.web.practiceapp.practice.record.CodeResolver
import dev.myclinic.scala.model.Shinryou
import cats.data.EitherT
import scala.concurrent.Future
import scala.language.implicitConversions

case class ShinryouMenu(at: LocalDate, visitId: Int):
  val auxMenu = PullDown.pullDownLink("その他", auxMenuItems)

  val ele = div(
    a("[診療行為]", onclick := (doRegular _)),
    auxMenu
  )

  def auxMenuItems: List[(String, () => Unit)] =
    List(
      "検査" -> (doKensa _),
      "検索入力" -> (doSearch _),
      "重複削除" -> (doDeleteDuplicate _),
      "全部コピー" -> (doCopyAll _)
    )

  def doRegular(): Unit =
    for regulars <- Api.getShinryouRegular()
    yield
      val dlog = new RegularDialog(
        regulars("left"),
        regulars("right"),
        regulars("bottom"),
        at,
        visitId
      )
      dlog.open

  def doKensa(): Unit =
    for config <- Api.getShinryouKensa()
    yield
      val dlog = KensaDialog(config, at, visitId)
      dlog.open

  def doSearch(): Unit =
    val dlog = SearchDialog(at, visitId)
    dlog.open

  def doDeleteDuplicate(): Unit =
    for
      shinryouList <- Api.listShinryouForVisit(visitId)
      (dups, _) = shinryouList.foldLeft(
        (List.empty[Shinryou], Set.empty[Int])
      ) { case ((dups, shinryoucodes), shinryou) =>
        val code = shinryou.shinryoucode
        if shinryoucodes.contains(code) then (dups :+ shinryou, shinryoucodes)
        else (dups, shinryoucodes + code)
      }
      _ <- dups
        .map(shinryou => Api.deleteShinryou(shinryou.shinryouId))
        .sequence
    yield dups.foreach(PracticeBus.shinryouDeleted.publish(_))

  def doCopyAll(): Unit =
    PracticeBus.copyTarget match {
      case None => ShowMessage.showError("コピー先をみつけられません。")
      case Some(targetVisitId) if targetVisitId == visitId =>
        ShowMessage.showError("同じ診察にコピーはできません。")
      case Some(targetVisitId) =>
        val op =
          for
            visit <- EitherT.right[String](Api.getVisit(visitId))
            targetAt = visit.visitedDate
            srcShinryouList <- EitherT.right(Api.listShinryouForVisit(visitId))
            srcShinryoucodes = srcShinryouList.map(_.shinryoucode)
            dstShinryoucodes <- srcShinryoucodes
              .map(CodeResolver.resolveShinryoucode(_, targetAt))
              .sequence
            dstShinryouReqs = dstShinryoucodes.map(code =>
              Shinryou(0, targetVisitId, code)
            )
            dstShinryouExList <- EitherT.right(
              CreateHelper.batchEnterShinryou(dstShinryouReqs)
            )
          yield dstShinryouExList.foreach(
            PracticeBus.shinryouEntered.publish(_)
          )
        for result <- op.value
        yield result match {
          case Left(msg) => ShowMessage.showError(msg)
          case Right(_)  => ()
        }
    }
