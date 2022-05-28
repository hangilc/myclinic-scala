package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import dev.myclinic.scala.model.ShinryouEx
import dev.myclinic.scala.model.CreateConductRequest
import dev.myclinic.scala.model.ConductKind
import dev.myclinic.scala.model.ConductShinryou

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
    val (names, kotsuen): (List[String], Option[Boolean]) =
      panel.selected.foldLeft[(List[String], Boolean)]((List.empty, false)) { case ((n, k), name) =>
        name match {
          case "骨塩定量" => (n, true)
          case _      => (n :+ name, k)
        }
      }
    for map <- Api.batchResolveShinryoucodeByName(names, at)
    yield
      val unresolved = (map.collect {
        case (name, code) if code == 0 => name
      }).toList
      if unresolved.isEmpty then
        val codes = map.values.toList
        for
          shinryouIds <- Api.batchEnterShinryou(visitId, codes)
          masterMap <- Api.batchResolveShinryouMaster(codes, at)
          shinryouList <- Api.batchGetShinryou(shinryouIds)
        yield
          shinryouList.foreach(s =>
            val master = masterMap(s.shinryoucode)
            PracticeBus.shinryouEntered.publish(ShinryouEx(s, master))
          )
          dlog.close()
      else
        val msg = "以下の診療行為のコードを取得できませんでした。\n" +
          unresolved.mkString("\n")
        ShowMessage.showError(msg)

  def kotsuenReq: CreateConductRequest =
    val shinryouName = "骨塩定量ＭＤ法"
    val kizaiName = "四ツ切"
    CreateConductRequest(
      visitId,
      ConductKind.Gazou.code,
      Some("骨塩定量に使用")
    )

