package dev.myclinic.scala.web.practiceapp.practice.record.shinryou

import dev.fujiwara.domq.all.{*, given}
import cats.data.EitherT
import dev.myclinic.scala.webclient.{Api, global}
import java.time.LocalDate
import cats.syntax.all.*
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.record.CodeResolver
import dev.myclinic.scala.web.practiceapp.practice.record.CreateHelper
import scala.language.implicitConversions

case class KensaDialog(
    config: Map[String, List[String]],
    at: LocalDate,
    visitId: Int
):
  val panel = KensaPanel(config)
  val dlog = new ModalDialog3()
  dlog.title("検査入力")
  dlog.body(panel.ele)
  dlog.commands(
    button("セット検査", onclick := (() => panel.checkPreset)),
    button("入力", onclick := (onEnter _)),
    button("クリア", onclick := (() => panel.clear)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def open: Unit =
    dlog.open()

  def onEnter(): Unit =
    val names = panel.selected
    val op =
      for
        shinryouExList <- CreateHelper.batchEnterShinryouByName(names, at, visitId)
      yield
        shinryouExList.foreach(PracticeBus.shinryouEntered.publish(_))
        dlog.close()
    for result <- op.value
    yield result match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(_) => ()
    }
