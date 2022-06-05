package dev.myclinic.scala.web.practiceapp.practice.patientmanip

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Text
import scala.concurrent.Future
import dev.myclinic.scala.webclient.{Api, global}

case class SearchTextDialog(patientId: Int):
  val form = new SearchForm[Text](
    text => text.content,
    text => if text.trim.isEmpty then Future.successful(List.empty)
      else Api.searchTextForPatient(text.trim, patientId)
  )
  form.ui.form(button("閉じる", onclick := (close _)))
  val dlog = new ModalDialog3()
  dlog.title(innerText := "文章検索")
  dlog.body(form.ele)

  def open(): Unit = 
    dlog.open()

  def close(): Unit =
    dlog.close()

