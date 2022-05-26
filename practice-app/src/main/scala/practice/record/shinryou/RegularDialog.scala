package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate

class RegularDialog(
    leftNames: List[String],
    rightNames: List[String],
    bottomNames: List[String],
    at: LocalDate
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
    val names: List[String] = panel.selected
    for map <- Api.batchResolveShinryoucodeByName(names, at)
    yield
      val unresolved = (map.collect {
        case (name, code) if code == 0 => name
      }).toList
      if unresolved.isEmpty then
        ???
        dlog.close()
      else
        val msg = "以下の診療行為のコードを取得できませんでした。\n" + 
          unresolved.mkString("\n")
        ShowMessage.showError(msg)
