package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.SearchFormPaging
import dev.myclinic.scala.model.*
import dev.myclinic.scala.webclient.{Api, global}
import dev.fujiwara.domq.SearchFormPagingConfig

class WholeTextSearchDialog:
  import WholeTextSearchDialog.NavUI
  val dlog = new ModalDialog3()
  given SearchFormPagingConfig with
    override def searchTextInputCssClass = Some("search-text-input")
  val form = new SearchFormPaging[(Text, Visit, Patient, String)](
    text => Api.countSearchTextGlobally(text),
    (text, limit, offset) => {
      for
        textVisits <- Api
          .searchTextGlobally(text.trim, limit, offset)
      yield textVisits.map { case (t, v, p) =>
        (t, v, p, text)
      }
    },
    (text, visit, patient, searchText) => div(text.content),
    new NavUI()
  )
  form.form(button("閉じる", onclick := (() => dlog.close())))
  dlog.title("全文検索")
  dlog.body(form.ele)

  def open(): Unit =
    dlog.open()

object WholeTextSearchDialog:
  def open(): Unit =
    val dlog = new WholeTextSearchDialog()
    dlog.open()

  case class NavUI( ) extends dev.fujiwara.domq.NavUI:
    val gotoFirstLink = None
    val gotoPrevLink = Some(a("<"))
    val gotoNextLink = Some(a(">"))
    val gotoLastLink = None
    val infoWrapper = Some(span)
    val ele = div(cls := "nav", gotoPrevLink.get, infoWrapper.get, gotoNextLink.get)    
