package dev.myclinic.scala.web.practiceapp.practice.record.conduct

import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import cats.data.EitherT
import cats.syntax.all.*
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.practiceapp.practice.record.shinryou.RequestHelper

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
    for
      targetVisitId <- EitherT
        .fromOption[Future](PracticeBus.copyTarget, "コピー先をみつけられませんでした。")
      srcConductIds <- EitherT.right(
        Api.listConductForVisit(visitId).map(_.map(_.conductId))
      )
      srcConductExList <- EitherT.right(
        srcConductIds.map(cid => Api.getConductEx(cid)).sequence
      )
    yield ???

  def dstConductReq(src: ConductEx, targetVisitId: Int): CreateConductRequest =
    def toShinryou(s: ConductShinryouEx): ConductShinryou =
      ConductShinryou(0, 0, s.shinryoucode)
    def toDrug(s: ConductDrugEx): ConductDrug =
      ConductDrug(0, 0, s.iyakuhincode, s.amount)
    def toKizai(s: ConductKizaiEx): ConductKizai =
      ConductKizai(0, 0, s.kizaicode, s.amount)
    CreateConductRequest(
      targetVisitId,
      src.kind.code,
      src.gazouLabel,
      src.shinryouList.map(toShinryou _),
      src.drugs.map(toDrug _),
      src.kizaiList.map(toKizai _)
    )
