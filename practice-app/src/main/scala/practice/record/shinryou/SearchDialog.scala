package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{ShinryouMaster, Shinryou}
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.record.CreateHelper
import scala.language.implicitConversions

case class SearchDialog(at: LocalDate, visitId: Int):
  val form = new SearchForm[ShinryouMaster](
    master => div(innerText := master.name),
    text => Api.searchShinryouMaster(text, at)
  )
  val dlog = new ModalDialog3()
  dlog.title("診療行為検索入力")
  dlog.body(form.ele)
  dlog.commands(
    button("入力", onclick := (doEnter _)),
    button("閉じる", onclick := (() => dlog.close()))
  )

  def open: Unit =
    dlog.open()

  def doEnter(): Unit =
    form.selected.foreach(master => {
      val shinryou = Shinryou(0, visitId, master.shinryoucode)
      for
        shinryouEx <- CreateHelper.enterShinryou(shinryou)
      yield
        PracticeBus.shinryouEntered.publish(shinryouEx)
    })
