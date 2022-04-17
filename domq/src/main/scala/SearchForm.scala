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

trait SelectionProvider[S, T]:
  def setItems[Src](
      s: S,
      items: List[Src],
      toLabel: Src => String,
      toValue: Src => T
  ): Unit
  def selected(s: S): Option[T]

class SearchFormEngine[I, T, SearchResult, D, S](
    input: I,
    trigger: T,
    selection: S,
    search: String => Future[List[SearchResult]],
    toLabel: SearchResult => String,
    toValue: SearchResult => D
)(using
    inputProvider: InputProvider[I],
    triggerProvider: TriggerProvider[T],
    selectionProvider: SelectionProvider[S, D]
):
  var onSearchDone: () => Unit = () => ()
  triggerProvider.onSearch(
    trigger,
    () => {
      val text = inputProvider.getText(input)
      for result <- search(text)
      yield
        selectionProvider.setItems(selection, result, toLabel, toValue)
        onSearchDone()
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
  given [D]: SelectionProvider[Selection[D], D] with
    def setItems[Src](
        s: Selection[D],
        items: List[Src],
        toLabel: Src => String,
        toValue: Src => D
    ): Unit =
      s.clear()
      s.addAll(items, toLabel, toValue)
    def selected(s: Selection[D]): Option[D] =
      s.marked

class SearchFormElementsBase[D]:
  val input: HTMLInputElement = inputText
  val button: HTMLButtonElement = Html.button("検索")
  val selection: Selection[D] = new Selection[D]

class SearchFormBase[Src, D](
    toLabel: Src => String,
    toValue: Src => D,
    search: String => Future[List[Src]]
):
  val ui = new SearchFormElementsBase[D]
  val ele = div(
    div(ui.input, ui.button),
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(
    ui.input,
    ui.button,
    ui.selection,
    search,
    toLabel,
    toValue
  )
  def selected: Option[D] = engine.selected

class SearchFormElements[D] extends SearchFormElementsBase[D]:
  val form = Html.form(input, button)

class SearchForm[Src, D](
    toLabel: Src => String,
    toValue: Src => D,
    search: String => Future[List[Src]]
):
  val ui = new SearchFormElements[D]
  val ele = div(
    ui.form,
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(
    ui.input,
    ui.form,
    ui.selection,
    search,
    toLabel,
    toValue
  )
  def selected: Option[D] = engine.selected
  def onSelect(handler: D => Unit): Unit =
    ui.selection.addSelectEventHandler(handler)
