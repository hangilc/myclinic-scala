package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.ModalDialog3
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.searchform.SearchForm
import dev.myclinic.scala.webclient.{Api, global}

object ShohouSampleDialog:

  def open(): Unit =
    val searchForm = SearchForm[String, String](
      identity,
      identity,
      Api.searchShohouSample _
    )
    searchForm.onSelect(item => 
      println(item)  
    )
    val dlog = new ModalDialog3
    dlog.title(innerText := "登録薬剤検索")
    dlog.body(
      searchForm.ele
    )
    dlog.commands(
      button("閉じる", onclick := (dlog.close _))
    )
    dlog.open()

