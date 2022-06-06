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
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.SelectionModifier
import dev.fujiwara.domq.Selection.given

trait InputProvider[I]:
  def getText(input: I): String

trait TriggerProvider[T]:
  def onSearch(t: T, cb: () => Unit): Unit

class SearchFormEngine[Input, Trigger, T](
    input: Input,
    trigger: Trigger,
    selection: Selection[T],
    search: String => Future[List[T]],
    toElement: T => HTMLElement
)(using
    inputProvider: InputProvider[Input],
    triggerProvider: TriggerProvider[Trigger]
):
  var onSearchDone: () => Unit = () => ()
  triggerProvider.onSearch(
    trigger,
    () => {
      val text = inputProvider.getText(input)
      for result <- search(text)
      yield
        setItems(result)
        onSearchDone()
    }
  )

  def setItems(result: List[T]): Unit =
    selection.clear()
    selection.addAll(result.map(v => (toElement(v), v)))

  def selected: Option[T] = selection.marked

object Implicits:
  given InputProvider[HTMLInputElement] with
    def getText(input: HTMLInputElement): String = input.value
  given TriggerProvider[HTMLButtonElement] with
    def onSearch(button: HTMLButtonElement, cb: () => Unit): Unit =
      button(onclick := cb)
  given TriggerProvider[HTMLFormElement] with
    def onSearch(form: HTMLFormElement, cb: () => Unit): Unit =
      form(onsubmit := cb)

class SearchFormElementsBase[T](using SelectionModifier):
  val input: HTMLInputElement = inputText
  val button: HTMLButtonElement = Html.button("検索", attr("type") := "submit")
  val selection: Selection[T] = new Selection[T]

class SearchFormBase[T](
    toElement: T => HTMLElement,
    search: String => Future[List[T]]
)(using SelectionModifier):
  val ui = new SearchFormElementsBase[T]
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
    toElement
  )
  def selected: Option[T] = engine.selected

class SearchFormElements[T](using SelectionModifier)
    extends SearchFormElementsBase[T]:
  val form = Html.form(input, button)

class SearchForm[T](
    toElement: T => HTMLElement,
    search: String => Future[List[T]]
)(using SelectionModifier):
  val ui = new SearchFormElements[T]
  val ele = div(
    ui.form(cls := "domq-search-form-form"),
    ui.selection.ele(cls := "domq-search-form-selection")
  )
  import Implicits.given
  val engine = new SearchFormEngine(
    ui.input,
    ui.form,
    ui.selection,
    search,
    toElement
  )
  def initFocus(): Unit = ui.input.focus()
  def selected: Option[T] = engine.selected
  def onSelect(handler: T => Unit): Unit =
    ui.selection.addSelectEventHandler(handler)

object SearchForm:
  def withToElement[T](using SelectionModifier)(
      toElement: T => HTMLElement,
      search: String => Future[List[T]]
  ): SearchForm[T] =
    new SearchForm(toElement, search)

  def apply[T](using SelectionModifier)(
      toLabel: T => String,
      search: String => Future[List[T]]
  ): SearchForm[T] =
    new SearchForm(t => div(innerText := toLabel(t)), search)
