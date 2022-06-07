package dev.fujiwara.domq

import scala.concurrent.Future
import org.scalajs.dom.HTMLElement
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global

trait SearchFormPagingConfig:
  def itemsPerPage: Int = 10

case class SearchFormPaging[T](
    countApi: String => Future[Int],
    fetchApi: (String, Int, Int) => Future[List[T]],
    render: T => HTMLElement,
    navUI: NavUI
)(using config: SearchFormPagingConfig):
  val form = Html.form
  val input = Html.input
  val button = Html.button(attr("type") := "submit")
  val selection = Selection[T]()
  val engine = Nav(navUI)
  private var searchText: String = ""
  val ele = div(
    form(input, button("検索")),
    navUI.ele,
    selection.ele
  )

  form(onsubmit := (doCount _))
  engine.onPageChanged {
    case None => selection.clear()
    case Some(page) => 
      for
        result <- fetchApi(searchText, config.itemsPerPage, page * config.itemsPerPage)
      yield
        selection.clear()
        selection.addAll(result.map(t => (render(t), t)))
  }

  def initFocus(): Unit = input.focus()

  def doCount(): Unit =
    val text = input.value.trim
    if !text.isEmpty then
      searchText = text
      for
        c <- countApi(text)
      yield engine.init(NavEngine.calcNumPages(c, config.itemsPerPage), Some(0))

object SearchFormPaging:
  given SearchFormPagingConfig = new SearchFormPagingConfig {}
