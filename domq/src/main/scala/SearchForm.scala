package dev.fujiwara.domq.searchform

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Html, Selection}
import scala.concurrent.Future
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits.global
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLButtonElement
import org.scalajs.dom.HTMLFormElement

trait InputProvider[I]:
  def getText(input: I): String

trait TriggerProvider[T]:
  def onSearch(t: T, cb: () => Unit): Unit

trait SelectionProvider[S[_]]:
  def setItems[D](s: S[D], items: List[D]): Unit
  def selected[D](s: S[D]): Option[D]

class SearchFormEngine[I, T, S[_], D](
    input: I,
    trigger: T,
    selection: S[D],
    search: String => Future[List[D]]
)(using
    inputProvider: InputProvider[I],
    triggerProvider: TriggerProvider[T],
    selectionProvider: SelectionProvider[S]
):
  triggerProvider.onSearch(
    trigger,
    () => {
      val text = inputProvider.getText(input)
      for result <- search(text)
      yield selectionProvider.setItems(selection, result)
    }
  )

  def selected: Option[D] = selectionProvider.selected(selection)

object Implicits:
  given InputProvider[HTMLInputElement] with
    def getText(input: HTMLInputElement): String = input.value
  given TriggerProvider[HTMLButtonElement] with
    def onSearch(button: HTMLButtonElement, cb: () => Unit): Unit =
      button(onclick := cb)
  given TriggerProvider[HTMLFormElement] with
    def onSearch(form: HTMLFormElement, cb: () => Unit): Unit =
      form(onsubmit := cb)
  given SelectionProvider[Selection] with
    def setItems[D](s: Selection[D], items: List[D]): Unit =
      s.clear()
      s.addAll(items)
    def selected[D](s: Selection[D]): Option[D] =
      s.selected

class SearchFormElements[D]:
  val input: HTMLInputElement = inputText
  val button: HTMLButtonElement = Html.button("検索")
  val selection: Selection[D] = Selection[D]()

class SearchFormBasic[D]:
  val search: String => Future[List[D]] = _ => Future.successful(List.empty)
  val ui = new SearchFormElements[D]
  val ele = div(
    div(ui.input, ui.button),
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(ui.input, ui.button, ui.selection, search)
  def selected: Option[D] = engine.selected

class SearchForm[D]:
  val search: String => Future[List[D]] = _ => Future.successful(List.empty)
  val ui = new SearchFormElements[D]
  val form: HTMLFormElement = Html.form
  val ele = div(
    form(ui.input, ui.button),
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(ui.input, form, ui.selection, search)
  def selected: Option[D] = engine.selected


