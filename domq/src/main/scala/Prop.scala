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


class InputUpdater[M](modelOpt: Option[M]):
  import dev.fujiwara.domq.prop.{*, given}
  object UpdateInputResult

  given [E, T, P <: Prop[M, E, T]]: ToListElementConstraint[P, UpdateInputResult.type] with
    def convert(p: P): UpdateInputResult.type =
      p.inputSpec.updateBy(modelOpt)
      UpdateInputResult

  def update[H, T <: Tuple](props: H *: T)(
    using ToListElementConstraint[H, UpdateInputResult.type],
      ToListConstraint[T, UpdateInputResult.type]
  ): Unit =
    summon[ToListConstraint[H *: T, UpdateInputResult.type]].convert(props)

class DispUpdater[M](modelOpt: Option[M]):
  import dev.fujiwara.domq.prop.{*, given}
  object UpdateDispResult

  given [E, T, P <: Prop[M, E, T]]: ToListElementConstraint[P, UpdateDispResult.type] with
    def convert(p: P): UpdateDispResult.type =
      p.dispSpec.updateBy(modelOpt)
      UpdateDispResult

  def update[Tup <: Tuple](props: Tup)(
    using c: ToListConstraint[Tup, UpdateDispResult.type]
  ): this.type =
    c.convert(props)
    this

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

  type ResultOf[H] = H match {
    case Prop[m, e, t] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[m, e, t] => p.inputSpec.validate()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))
