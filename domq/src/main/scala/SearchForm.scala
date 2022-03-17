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

trait SelectionProvider[S[_, _]]:
  def setItems[Src, D](s: S[Src, D], items: List[Src]): Unit
  def selected[Src, D](s: S[Src, D]): Option[D]

class SearchFormEngine[I, T, Src, D, S[_, _]](
    input: I,
    trigger: T,
    selection: S[Src, D],
    search: String => Future[List[Src]]
)(using
    inputProvider: InputProvider[I],
    triggerProvider: TriggerProvider[T],
    selectionProvider: SelectionProvider[S]
):
  var onSearchDone: () => Unit = () => ()
  triggerProvider.onSearch(
    trigger,
    () => {
      val text = inputProvider.getText(input)
      for result <- search(text)
      yield 
        selectionProvider.setItems(selection, result)
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
  given SelectionProvider[Selection] with
    def setItems[Src, D](s: Selection[Src, D], items: List[Src]): Unit =
      s.clear()
      s.addAll(items)
    def selected[Src, D](s: Selection[Src, D]): Option[D] =
      s.selected

class SearchFormElementsBase[Src, D](mapper: Src => D):
  val input: HTMLInputElement = inputText
  val button: HTMLButtonElement = Html.button("検索")
  val selection: Selection[Src, D] = new Selection[Src, D](mapper)

class SearchFormBase[Src, D](mapper: Src => D, search: String => Future[List[Src]]):
  val ui = new SearchFormElementsBase[Src, D](mapper)
  val ele = div(
    div(ui.input, ui.button),
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(ui.input, ui.button, ui.selection, search)
  def selected: Option[D] = engine.selected

class SearchFormElements[Src, D](mapper: Src => D) extends SearchFormElementsBase[Src, D](mapper):
  val form = Html.form(input, button)

class SearchForm[Src, D](mapper: Src => D, search: String => Future[List[Src]]):
  val ui = new SearchFormElements[Src, D](mapper)
  val ele = div(
    ui.form,
    ui.selection.ele
  )
  import Implicits.given
  val engine = new SearchFormEngine(ui.input, ui.form, ui.selection, search)
  def selected: Option[D] = engine.selected
  def onSelect(handler: D => Unit): Unit = ui.selection.addSelectEventHandler(handler)


