package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import cats.data.EitherT
import cats.syntax.all.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.practiceapp.practice.record.CodeResolver
import dev.myclinic.scala.web.practiceapp.practice.record.CreateHelper
import scala.language.implicitConversions

case class ConductMenu(at: LocalDate, visitId: Int):
  val link = PullDownLink("処置")
  link.setBuilder(menuItems)
  val workarea = div
  val ele = div(link.ele, workarea)

  def menuItems: List[(String, () => Unit)] =
    List(
      "Ｘ線検査追加" -> (doXp _),
      "注射追加" -> (doConductDrug _),
      "全部コピー" -> (doCopyAll _)
    )

  def doXp(): Unit =
    val w = XpWidget(at, visitId, _.close())
    workarea.prepend(w.ele)

  def doConductDrug(): Unit =
    val w = ConductDrugWidget(at, visitId, _.close())
    workarea.prepend(w.ele)

  def doCopyAll(): Unit =
    val op =
      for
        targetVisitId <- EitherT
          .fromOption[Future](PracticeBus.copyTarget, "コピー先をみつけられませんでした。")
        srcConductIds <- EitherT.right(
          Api.listConductForVisit(visitId).map(_.map(_.conductId))
        )
        srcConductExList <- EitherT.right(srcConductIds.map(cid => Api.getConductEx(cid)).sequence)
        reqs <- srcConductExList.map(c => dstConductReq(c, targetVisitId)).sequence
        dstConductExList <- EitherT.right(CreateHelper.batchEnterConduct(reqs))
      yield dstConductExList.foreach(PracticeBus.conductEntered.publish(_))
    for
      result <- op.value
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(_) => ()
    }

  def dstConductReq(
      src: ConductEx,
      targetVisitId: Int
  ): EitherT[Future, String, CreateConductRequest] =
    def toShinryou(
        s: ConductShinryouEx
    ): EitherT[Future, String, ConductShinryou] =
      for newcode <- CodeResolver.resolveShinryoucode(s.shinryoucode, at)
      yield ConductShinryou(0, 0, newcode)
    def toDrug(s: ConductDrugEx): EitherT[Future, String, ConductDrug] =
      for newcode <- CodeResolver.resolveIyakuhincode(s.iyakuhincode, at)
      yield ConductDrug(0, 0, newcode, s.amount)
    def toKizai(s: ConductKizaiEx): EitherT[Future, String, ConductKizai] =
      for newcode <- CodeResolver.resolveKizaicode(s.kizaicode, at)
      yield ConductKizai(0, 0, newcode, s.amount)
    for
      shinryouList <- src.shinryouList.map(toShinryou _).sequence
      drugList <- src.drugs.map(toDrug _).sequence
      kizaiList <- src.kizaiList.map(toKizai _).sequence
    yield CreateConductRequest(
      targetVisitId,
      src.kind.code,
      src.gazouLabel,
      shinryouList,
      drugList,
      kizaiList
    )
