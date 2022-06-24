package dev.fujiwara.dateinput

import java.time.LocalDate
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.Era

class DateInputFormDialog(
    init: Option[LocalDate] = None,
    title: String = "日付の入力",
    gengouList: List[Era] = DateInputForm.defaultGengouList,
    allowNone: Boolean = true
)(using InitNoneConverter):
  private val onEnterPublisher = new LocalEventPublisher[Option[LocalDate]]
  val form = new DateInputForm(init)
  val errBox = ErrorBox()
  val dlog = new ModalDialog3()
  dlog.title(title)
  dlog.body(form.ele, errBox.ele)
  dlog.commands(
    button("入力", onclick := (doEnter _)),
    button("キャンセル", onclick := (() => dlog.close()))
  )

  def onEnter(handler: Option[LocalDate] => Unit): Unit =
    onEnterPublisher.subscribe(handler)

  def open(): Unit =
    dlog.open()

  private def doEnter(): Unit =
    form.validated match {
      case Right(dOpt) => 
        if dOpt.isEmpty && !allowNone then
          errBox.show("日付が入力されていません。")
        else
          dlog.close()
          onEnterPublisher.publish(dOpt)
      case Left(msg) => 
        errBox.show(msg)
    }
