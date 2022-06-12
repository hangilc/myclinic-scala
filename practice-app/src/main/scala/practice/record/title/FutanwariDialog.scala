package dev.myclinic.scala.web.practiceapp.practice.record.title

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.Html
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}

case class FutanwariDialog(visit: Visit, onChange: (FutanwariDialog => Unit)):
  val input = Html.input
  val dlog = new ModalDialog3()
  dlog.title("負担割オーバーライド")
  dlog.body(
    div(
      "変更後負担割：",
      input(cls := "practice-futanwari-override-dialog-input",
        value := visit.futanWariOverride.map(_.toString).getOrElse("")),
      "割"
    )
  )
  dlog.commands(
    button("入力", onclick := (doEnter _)),
    button("キャンセル", (onclick := (() => dlog.close())))
  )

  def open(): Unit = 
    dlog.open()

  def close(): Unit = 
    dlog.close()

  def doEnter(): Unit =
    inputFutanwari match {
      case Left(msg) => ShowMessage.showError(msg)
      case Right(fOpt) =>
        val attr = visit.attributes.copy(futanWari = fOpt)
        val newVisit = visit.copy(attributesStore = attr.asStore)
        for
          _ <- Api.updateVisit(newVisit)
        yield onChange(this)
    }

  def inputFutanwari: Either[String, Option[Int]] =
    input.value match {
      case "" => Right(None)
      case v =>
        v.toIntOption match {
          case None => Left("入力が不適切です。")
          case Some(f) => Right(Some(f))
        }
    }
    


