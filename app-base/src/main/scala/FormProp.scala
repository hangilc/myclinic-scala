package dev.myclinic.scala.web.appbase.formprop

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.*
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.fujiwara.dateinput.DateOptionInput
import dev.myclinic.scala.model.ValidUpto

case class Prop[T, E](
    label: String,
    element: HTMLElement,
    validator: () => ValidatedResult[E, T]
)

trait LabelElementExtractor[T]:
  def extract(t: T): (String, HTMLElement)

trait LabelElementListExtractor[T]:
  def extract(t: T): List[(String, HTMLElement)]

type ResultOf[H] = H match {
  case Prop[t, e] => ValidatedResult[e, t]
}

object Prop:
  given [T, E]: LabelElementExtractor[Prop[T, E]] with
    def extract(p: Prop[T, E]): (String, HTMLElement) =
      (p.label, p.element)

  given LabelElementListExtractor[EmptyTuple] with
    def extract(t: EmptyTuple): List[(String, HTMLElement)] =
      List.empty

  given [H: LabelElementExtractor, T <: Tuple: LabelElementListExtractor]
      : LabelElementListExtractor[H *: T] with
    def extract(t: H *: T): List[(String, HTMLElement)] =
      summon[LabelElementExtractor[H]].extract(t.head) ::
        summon[LabelElementListExtractor[T]].extract(t.tail)

  def labelElements[
      H: LabelElementExtractor,
      T <: Tuple: LabelElementListExtractor
  ](tuple: H *: T): List[(String, HTMLElement)] =
    summon[LabelElementListExtractor[H *: T]].extract(tuple)

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[t, e] => p.validator()
    }

  def resultsOf(props: Tuple) =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))
 
  def apply[T, E](
      label: String,
      e: HTMLInputElement,
      validator: String => ValidatedResult[E, T]
  ): Prop[T, E] =
    new Prop[T, E](label, e, () => validator(e.value))

  def radio[T, E](
      label: String,
      data: List[(String, T)],
      init: T,
      validator: T => ValidatedResult[E, T]
  ): Prop[T, E] =
    val radioGroup = RadioGroup(data, initValue = Some(init))
    new Prop[T, E](label, radioGroup.ele, () => validator(radioGroup.value))

  def date[E](
      label: String,
      init: Option[LocalDate],
      validator: Option[LocalDate] => ValidatedResult[E, LocalDate]
  ): Prop[LocalDate, E] =
    val dateInput = DateOptionInput(init)
    new Prop[LocalDate, E](
      label,
      dateInput.ele,
      () => validator(dateInput.value)
    )

  def validUpto[E](
      label: String,
      init: Option[LocalDate],
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto]
  ): Prop[ValidUpto, E] =
    val dateInput = DateOptionInput(init)
    new Prop[ValidUpto, E](
      label,
      dateInput.ele,
      () => validator(dateInput.value)
    )
