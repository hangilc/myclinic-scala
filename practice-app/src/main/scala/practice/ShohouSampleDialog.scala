package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.ModalDialog3
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.searchform.SearchForm
import dev.myclinic.scala.webclient.{Api, global}
import dev.myclinic.scala.formatshohousen.FormatUtil
import scala.language.implicitConversions

object ShohouSampleDialog:

  def open(): Unit =
    var index: Int = 0
    def getIndex: String = 
      index += 1
      FormatUtil.indexRep(index, 5)
    val ta = textarea
    val searchForm = SearchForm[String](
      identity,
      Api.searchShohouSample _
    )
    searchForm.onSelect(item => 
      ta.value = ta.value + getIndex + item + "\n"
    )
    val dlog = new ModalDialog3
    dlog.title(innerText := "登録薬剤検索")
    dlog.body(
      ta(cls := "practice-shohou-sample-dialog-ta"),
      searchForm.ele
    )
    dlog.commands(
      button("閉じる", onclick := (dlog.close _))
    )
    dlog.open()

