package dev.fujiwara.domq

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global

class SearchForm[T]:
  val searchFormInput = new SearchFormInput
  val select = Selection[T]()
  val search: String => Future[List[T]] = _ => Future.successful(List.empty)
  val ele = div(
    searchFormInput.ele,
    select.ele
  )
  searchFormInput.onSearch = text => 
    for
      result <- search(text)
    yield
      select.clear()
      select.addAll(result)

class SearchFormInput:
  var onSearch: String => Unit = _ => ()
  val input = inputText
  val ele = form(onsubmit := (() => onSearch(input.value)), cls := "domq-search-form-input-form")(
    input(cls := "domq-search-form-input-input"),
    button("検索", attr("type") := "submit", cls := "domq-search-form-input-button")
  )