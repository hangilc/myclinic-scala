package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.validator.section.ValidatedResult

case class Prop[M, E, T](
    label: String,
    inputCreator: () => HTMLElement,
    updateInputBy: Option[M] => Unit,
    validator: () => ValidatedResult[E, T],
    dispCreator: () => HTMLElement,
    updateDispBy: Option[M] => Unit
)

object Prop:
  def apply[M, E, T](
      label: String,
      inputCreator: () => HTMLInputElement,
      modelValue: Option[M] => String,
      validator: String => ValidatedResult[E, T],
      dispCreator: () => HTMLElement,
      updateDispBy: Option[M] => Unit
  ): Prop[M, E, T] =
    lazy val inputElement: HTMLInputElement = inputCreator()
    lazy val dispElement: HTMLElement = dispCreator()
    new Prop(
      label,
      () => inputElement,
      mOpt => inputElement.value = modelValue(mOpt),
      () => validator(inputElement.value),
      () => dispElement,
      updateDispBy
    )

  def radio[M, E, T](
    label: String,
    data: List[(String, T)],
    initValue: T,
    modelValue: Option[M] => T,
    validator: () => ValidatedResult[E, T],
    dispRep: Option[M] => String,
    layout: RadioGroup[T] => HTMLElement = (rg => RadioGroup.defaultLayout[T](rg)),
  ) =
    lazy val radioGroup = RadioGroup[T](
      data, initValue = Some(initValue), layout = layout
    )
    lazy val dispElement = Html.span
    new Prop[M, E, T](
      label,
      () => radioGroup.ele,
      mOpt => radioGroup.check(modelValue(mOpt)),
      validator,
      () => dispElement,
      mOpt => dispElement.innerText = dispRep(mOpt)
    )

class PropsModel[M](var model: Option[M]):
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

  case class LabelInput(label: String, input: HTMLElement)

  given [E, T]: ToListElementConstraint[Prop[M, E, T], LabelInput] with
    def convert(t: Prop[M, E, T]): LabelInput =
      LabelInput(t.label, t.inputCreator())

  def formPanel[Head, Tail <: Tuple](props: Head *: Tail)(
    using ToListElementConstraint[Head, LabelInput],
      ToListConstraint[Tail, LabelInput]
  ): HTMLElement =
    val les = summon[ToListConstraint[Head *: Tail, LabelInput]].convert(props)
    val panel = DispPanel(form = true)
    les.foreach(li => panel.add(li.label, li.input))
    panel.ele

  case class LabelElement(label: String, element: HTMLElement)
  
  given [E, T]: ToListElementConstraint[Prop[M, E, T], LabelElement] with
    def convert(t: Prop[M, E, T]): LabelElement =
      LabelElement(t.label, t.dispCreator())

  def dispPanel[Head, Tail <: Tuple](props: Head *: Tail)(
    using ToListElementConstraint[Head, LabelElement],
      ToListConstraint[Tail, LabelElement]
  ): HTMLElement =
    val les = summon[ToListConstraint[Head *: Tail, LabelElement]].convert(props)
    val panel = DispPanel()
    les.foreach(le => panel.add(le.label, le.element))
    panel.ele

  object UpdateInputByResult

  given [E, T]: ToListElementConstraint[Prop[M, E, T], UpdateInputByResult.type] with
    def convert(p: Prop[M, E, T]): UpdateInputByResult.type =
      p.updateInputBy(model)
      UpdateInputByResult

  def updateInput[Head, Tail <: Tuple, M](props: Head *: Tail, model: Option[M])(
    using ToListElementConstraint[Head, UpdateInputByResult.type],
      ToListConstraint[Tail, UpdateInputByResult.type]
  ): Unit =
    summon[ToListConstraint[Head *: Tail, UpdateInputByResult.type]].convert(props)

  object UpdateDispByResult

  given [E, T]: ToListElementConstraint[Prop[M, E, T], UpdateDispByResult.type] with
    def convert(p: Prop[M, E, T]): UpdateDispByResult.type =
      p.updateDispBy(model)
      UpdateDispByResult

  def updateDisp[Head, Tail <: Tuple, M](props: Head *: Tail, model: Option[M])(
    using ToListElementConstraint[Head, UpdateDispByResult.type],
      ToListConstraint[Tail, UpdateDispByResult.type]
  ): Unit =
    summon[ToListConstraint[Head *: Tail, UpdateDispByResult.type]].convert(props)

  type ResultOf[H] = H match {
    case Prop[m, e, t] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[m, e, t] => p.validator()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))

object Props:
  case class Patient(lastName: String, firstName: String)

  val patientProps = (
    Prop[Patient, Nothing, String](
      "姓",
      () => Html.input,
      _.fold("")(_.lastName),
      () => ???,
      () => Html.span,
      _ => ()
    ),
    Prop[Patient, Nothing, String](
      "名",
      () => Html.input,
      _.fold("")(_.firstName),
      () => ???,
      () => Html.span,
      _ => ()
    ),
  )
  val pModel = new PropsModel(Some(patient))
  val ele = pModel.formPanel(patientProps)
  val patient = Patient("A", "B")
  pModel.updateInput(patientProps, Some(patient))
  val disp = pModel.dispPanel(patientProps)
  pModel.updateDisp(patientProps, Some(patient))
  val results = pModel.resultsOf(patientProps)


