package dev.fujiwara.domq.prop

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.validator.section.ValidatedResult

trait InputSpec[M, +E, T]:
  val ele: HTMLElement
  def updateBy(model: Option[M]): Unit
  def validate(): ValidatedResult[E, T]

  def addClass(className: String): InputSpec[M, E, T] =
    ele(cls := className)
    this

trait DispSpec[M]:
  val ele: HTMLElement
  def updateBy(model: Option[M]): Unit

trait Prop[M, +E, T]:
  def label: String
  lazy val inputSpec: InputSpec[M, E, T]
  lazy val dispSpec: DispSpec[M]

  def withInputElement(handler: HTMLElement => Unit): Prop[M, E, T] =
    handler(inputSpec.ele)
    this

  def withDispElement(handler: HTMLElement => Unit): Prop[M, E, T] =
    handler(dispSpec.ele)
    this

  def inputElementClass(className: String): Prop[M, E, T] =
    withInputElement(_(cls := className))

  def dispElementClass(className: String): Prop[M, E, T] =
    withDispElement(_(cls := className))

  def inputElement: HTMLElement = inputSpec.ele

  def dispElement: HTMLElement = dispSpec.ele


trait ToListElementConstraint[T, E]:
  def convert(t: T): E

type ToListElementConstraintGen =
  [E] =>> [T] =>> ToListElementConstraint[T, E]

trait ToListConstraint[T, E]:
  def convert(t: T): List[E]

given [E]: ToListConstraint[EmptyTuple, E] with
  def convert(t: EmptyTuple): List[E] = List.empty

type ToListConstraintGen = [E] =>> [T] =>> ToListConstraint[T, E]

given [E, H: ToListElementConstraintGen[E], T <: Tuple: ToListConstraintGen[
  E
]]: ToListConstraint[H *: T, E] with
  def convert(t: H *: T): List[E] =
    summon[ToListElementConstraint[H, E]].convert(t.head) ::
      summon[ToListConstraint[T, E]].convert(t.tail)

case class LabelInput(label: String, input: HTMLElement)

given [M, E, T, P <: Prop[M, E, T]]: ToListElementConstraint[P, LabelInput] with
  def convert(t: P): LabelInput =
    LabelInput(t.label, t.inputSpec.ele)

given [T <: HTMLElement]: ToListElementConstraint[(String, T), LabelInput] with
  def convert(t: (String, T)): LabelInput =
    LabelInput.apply.tupled(t)

case class LabelElement(label: String, element: HTMLElement)

given [M, E, T, P <: Prop[M, E, T]]: ToListElementConstraint[P, LabelElement]
  with
  def convert(t: P): LabelElement =
    LabelElement(t.label, t.dispSpec.ele)

given [T <: HTMLElement]: ToListElementConstraint[(String, T), LabelElement]
  with
  def convert(t: (String, T)): LabelElement =
    LabelElement.apply.tupled(t)

object UpdateInputByResult

object Prop:
  def formPanel[Head, Tail <: Tuple](props: Head *: Tail)(using
      ToListElementConstraint[Head, LabelInput],
      ToListConstraint[Tail, LabelInput]
  ): HTMLElement =
    val les = summon[ToListConstraint[Head *: Tail, LabelInput]].convert(props)
    val panel = DispPanel(form = true)
    les.foreach(li => panel.add(li.label, li.input))
    panel.ele

  def dispPanel[Head, Tail <: Tuple](props: Head *: Tail)(using
      ToListElementConstraint[Head, LabelElement],
      ToListConstraint[Tail, LabelElement]
  ): HTMLElement =
    val les =
      summon[ToListConstraint[Head *: Tail, LabelElement]].convert(props)
    val panel = DispPanel()
    les.foreach(le => panel.add(le.label, le.element))
    panel.ele

  // class InputUpdater[M](modelOpt: Option[M]):
  //   type UpdateResult[T] = T match {
  //     case Prop[M, e, t] => Unit
  //   }

  //   def update[T](t: T): UpdateResult[T] =
  //     t match {
  //       case p: Prop[M, e, t] => p.inputSpec.updateBy(modelOpt)
  //     }

  //   def update(props: Tuple): Tuple.Map[props.type, UpdateResult] =
  //     props.map[UpdateResult]([T] => (t: T) => update(t))

  // class DispUpdater[M](modelOpt: Option[M]):
  //   type UpdateResult[T] = T match {
  //     case Prop[M, e, t] => Unit
  //   }

  //   def update[T](t: T): UpdateResult[T] =
  //     t match {
  //       case p: Prop[M, e, t] => p.dispSpec.updateBy(modelOpt)
  //     }

  //   def update(props: Tuple): Tuple.Map[props.type, UpdateResult] =
  //     props.map[UpdateResult]([T] => (t: T) => update(t))

  // type ForEachType[T, M] = T match {
  //   case Prop[M, e, t] => Unit
  // }

  // type ForEachTypeWith = [M] =>> [T] =>> ForEachType[T, M]

  // def updateInputBy[M, T](t: T, modelOpt: Option[M]): ForEachType[T, M] =
  //   t match {
  //     case p: Prop[M, e, t] =>
  //       p.inputSpec.updateBy(modelOpt)
  //       ()
  //   }

  // def updateInput[M](
  //     props: Tuple,
  //     modelOpt: Option[M]
  // ): Tuple.Map[props.type, ForEachTypeWith[M]] =
  //   props.map[ForEachTypeWith[M]]([T] => (t: T) => updateInputBy(t, modelOpt))

  // def updateDispBy[M, T](t: T, modelOpt: Option[M]): ForEachType[T, M] =
  //   t match {
  //     case p: Prop[M, e, t] =>
  //       p.dispSpec.updateBy(modelOpt)
  //       ()
  //   }

  // def updateDisp[M](
  //     props: Tuple,
  //     modelOpt: Option[M]
  // ): Tuple.Map[props.type, ForEachTypeWith[M]] =
  //   props.map[ForEachTypeWith[M]]([T] => (t: T) => updateDispBy(t, modelOpt))

  type ResultOf[H] = H match {
    case Prop[m, e, t] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[m, e, t] => p.inputSpec.validate()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))

// class PropsModel[M](model: Option[M]):

//   object UpdateInputByResult

//   given [E, T, P[M, E, T] <: Prop[M, E, T]]
//       : ToListElementConstraint[P[M, E, T], UpdateInputByResult.type] with
//     def convert(p: P[M, E, T]): UpdateInputByResult.type =
//       p.inputSpec.updateBy(model)
//       UpdateInputByResult

//   def updateInput[Head, Tail <: Tuple, M](props: Head *: Tail)(using
//       ToListElementConstraint[Head, UpdateInputByResult.type],
//       ToListConstraint[Tail, UpdateInputByResult.type]
//   ): Unit =
//     summon[ToListConstraint[Head *: Tail, UpdateInputByResult.type]]
//       .convert(props)

//   object UpdateDispByResult

//   given [E, T, P[M, E, T] <: Prop[M, E, T]]
//       : ToListElementConstraint[P[M, E, T], UpdateDispByResult.type] with
//     def convert(p: P[M, E, T]): UpdateDispByResult.type =
//       p.dispSpec.updateBy(model)
//       UpdateDispByResult

//   def updateDisp[Head, Tail <: Tuple, M](props: Head *: Tail)(using
//       ToListElementConstraint[Head, UpdateDispByResult.type],
//       ToListConstraint[Tail, UpdateDispByResult.type]
//   ): Unit =
//     summon[ToListConstraint[Head *: Tail, UpdateDispByResult.type]]
//       .convert(props)

