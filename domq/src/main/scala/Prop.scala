package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.validator.section.ValidatedResult

case class Prop[M, E, T](
    label: String,
    inputCreator: () => HTMLElement,
    updateInputBy: Option[M] => Unit,
    validator: () => ValidatedResult[E, T]
)

object Prop:

  trait ToListElementConstraint[T, E]:
    def convert(t: T): E

  type ToListElementConstraintGen = [E] =>> [T] =>> ToListElementConstraint[T, E]

  trait ToListConstraint[T, E]:
    def convert(t: T): List[E]

  given [E]: ToListConstraint[EmptyTuple, E] with
    def convert(t: EmptyTuple): List[E] = List.empty

  type ToListConstraintGen = [E] =>> [T] =>> ToListConstraint[T, E]

  given [E, H: ToListElementConstraintGen[E], T <: Tuple: ToListConstraintGen[E]]
      : ToListConstraint[H *: T, E] with
    def convert(t: H *: T): List[E] =
      summon[ToListElementConstraint[H, E]].convert(t.head) ::
        summon[ToListConstraint[T, E]].convert(t.tail)

  given [M, E, T]: ToListElementConstraint[Prop[M, E, T], (String, HTMLElement)] with
    def convert(t: Prop[M, E, T]): (String, HTMLElement) =
      (t.label, t.inputCreator())

  def formPanel[Head, Tail <: Tuple](props: Head *: Tail)(
    using ToListElementConstraint[Head, (String, HTMLElement)],
      ToListConstraint[Tail, (String, HTMLElement)]
  ): HTMLElement =
    val les = summon[ToListConstraint[Head *: Tail, (String, HTMLElement)]].convert(props)
    val panel = DispPanel()
    les.foreach { (s, e) => panel.add(s, e) }
    panel.ele

  object UpdateInputByResult

  class InputUpdater[M](model: Option[M]):
    given [E, T]: ToListElementConstraint[Prop[M, E, T], UpdateInputByResult.type] with
      def convert(p: Prop[M, E, T]): UpdateInputByResult.type =
        p.updateInputBy(model)
        UpdateInputByResult

    def updateInput[Head, Tail <: Tuple](props: Head *: Tail)(
      using ToListElementConstraint[Head, UpdateInputByResult.type],
        ToListConstraint[Tail, UpdateInputByResult.type]
    ): Unit =
      summon[ToListConstraint[Head *: Tail, UpdateInputByResult.type]].convert(props)

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

  case class Patient(lastName: String, firstName: String)

  val patientProps = (
    Prop[Patient, Nothing, String](
      "姓",
      () => Html.input,
      _.fold("")(_.lastName),
      () => ???
    ),
    Prop[Patient, Nothing, String](
      "名",
      () => Html.input,
      _.fold("")(_.firstName),
      () => ???
    ),
  )
  val ele = formPanel(patientProps)
  val patient = Patient("A", "B")
  val inputUpdater = new InputUpdater[Patient](Some(patient))
  import inputUpdater.given
  inputUpdater.updateInput(patientProps)

