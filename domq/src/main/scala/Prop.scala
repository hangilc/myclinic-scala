package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.validator.section.ValidatedResult

case class Prop[M, E, T](
    label: String,
    inputCreator: () => HTMLElement,
    updateInputFrom: Option[M] => Unit,
    validator: () => ValidatedResult[E, T]
)

object Prop:
  class TupleToListConverter[E]:

    trait Extractor[T]:
      def extract(t: T): E

    trait ListExtractor[T]:
      def extract(t: T): List[E]

    given ListExtractor[EmptyTuple] with
      def extract(t: EmptyTuple): List[E] = List.empty

    given [Head: Extractor, Tail <: Tuple: ListExtractor]
        : ListExtractor[Head *: Tail] with
      def extract(tuple: Head *: Tail): List[E] =
        summon[Extractor[Head]].extract(tuple.head) ::
          summon[ListExtractor[Tail]].extract(tuple.tail)

    def convert[Head, Tail <: Tuple](tuple: Head *: Tail)(using
        extractor: Extractor[Head],
        listExtractor: ListExtractor[Tail]
    ): List[E] =
      summon[ListExtractor[Head *: Tail]].extract(tuple)

  object LabelElementExtractor
      extends TupleToListConverter[(String, HTMLElement)]:
    given [M, T, E]: Extractor[Prop[M, T, E]] with
      def extract(prop: Prop[M, T, E]): (String, HTMLElement) =
        (prop.label, prop.inputCreator())

    given Extractor[(String, HTMLElement)] with
      def extract(a: (String, HTMLElement)): (String, HTMLElement) = a

  def inputPanel[
      H,
      T <: Tuple
  ](
      props: H *: T
  )(using
      LabelElementExtractor.Extractor[H],
      LabelElementExtractor.ListExtractor[T]
  ): HTMLElement =
    val panel = DispPanel(form = true)
    LabelElementExtractor.convert(props).foreach { (s, e) =>
      panel.add(s, e)
    }
    panel.ele

  def apply[M, E, T](
      label: String,
      inputCreator: () => HTMLInputElement,
      modelValue: Option[M] => String,
      validator: String => ValidatedResult[E, T]
  ): Prop[M, E, T] =
    lazy val inputElement: HTMLInputElement = inputCreator()
    new Prop(
      label,
      () => inputElement,
      mOpt => inputElement.value = modelValue(mOpt),
      () => validator(inputElement.value)
    )
