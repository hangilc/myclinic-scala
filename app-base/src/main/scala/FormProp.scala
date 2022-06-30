package dev.myclinic.scala.web.appbase.formprop

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.*
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.domq.all.{*, given}
import java.time.LocalDate
import dev.fujiwara.dateinput.DateOptionInput
import dev.myclinic.scala.model.ValidUpto
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.kanjidate.KanjiDate

case class Prop[T, E, M](
    label: String,
    elementCreator: () => HTMLElement,
    validator: () => ValidatedResult[E, T],
    dispRep: Option[M] => String
)

object Prop:
  trait LabelElementExtractor[T]:
    def extract(t: T): (String, HTMLElement)

  trait LabelElementListExtractor[T]:
    def extract(t: T): List[(String, HTMLElement)]

  given [T, E, M]: LabelElementExtractor[Prop[T, E, M]] with
    def extract(p: Prop[T, E, M]): (String, HTMLElement) =
      (p.label, p.elementCreator())

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

  def panel[
      H: LabelElementExtractor,
      T <: Tuple: LabelElementListExtractor
  ](props: H *: T): HTMLElement =
    val dp = DispPanel(form = true)
    labelElements(props).foreach(dp.add.tupled)
    dp.ele

  type ResultOf[H] = H match {
    case Prop[t, e, ?] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[t, e, ?] => p.validator()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))
 
  def apply[T, E, M](
      label: String,
      elementCreator: () => HTMLInputElement,
      validator: String => ValidatedResult[E, T],
      dispRep: Option[M] => String
  ): Prop[T, E, M] =
    lazy val element: HTMLInputElement = elementCreator()
    new Prop[T, E, M](label, () => element, () => validator(element.value), dispRep)

  def radio[T, E, M](
      label: String,
      data: List[(String, T)],
      init: T,
      validator: T => ValidatedResult[E, T],
      dispRep: Option[M] => String
  ): Prop[T, E, M] =
    lazy val radioGroup = RadioGroup(data, initValue = Some(init))
    new Prop[T, E, M](label, () => radioGroup.ele, () => validator(radioGroup.value), dispRep)

  def date[E, M](
      label: String,
      init: Option[LocalDate],
      validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
      dispRep: Option[M] => String
  ): Prop[LocalDate, E, M] =
    lazy val dateInput = DateOptionInput(init)
    new Prop[LocalDate, E, M](
      label,
      () => dateInput.ele,
      () => validator(dateInput.value),
      dispRep
    )

  def validUpto[E, M](
      label: String,
      init: Option[LocalDate],
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
      dispRep: Option[M] => String
  ): Prop[ValidUpto, E, M] =
    lazy val dateInput = DateOptionInput(init)
    new Prop[ValidUpto, E, M](
      label,
      () => dateInput.ele,
      () => validator(dateInput.value),
      dispRep
    )

  def mRep[M, T](f: M => T, s: T => String = (t: T) => t.toString): Option[M] => String =
    mOpt =>
      mOpt.map(f andThen s).getOrElse("")

  def mRepDate[M](f: M => LocalDate): Option[M] => String =
    mRep[M, LocalDate](f, d => KanjiDate.dateToKanji(d))

  def mRepValidUpto[M](f: M => ValidUpto): Option[M] => String =
    mOpt =>
      (mOpt.map(f(_).value).map {
        case None => ""
        case Some(d) => KanjiDate.dateToKanji(d)
      }).getOrElse("")
