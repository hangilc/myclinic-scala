package dev.fujiwara.domq.prop

import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.validator.section.ValidatedResult

trait InputSpec[M, E, T]:
  def createElement: HTMLElement
  def updateBy(model: Option[M]): Unit
  def validate: ValidatedResult[E, T]

trait DispSpec[M]:
  def createElement: HTMLElement
  def updateBy(model: Option[M]): Unit

case class Prop[M, E, T](
    label: String,
    inputSpec: InputSpec[M, E, T],
    dispSpec: DispSpec[M]
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
      (t.label, t.inputSpec.createElement)

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
        p.inputSpec.updateBy(model)
        UpdateInputByResult

    def updateInput[Head, Tail <: Tuple](props: Head *: Tail)(
      using ToListElementConstraint[Head, UpdateInputByResult.type],
        ToListConstraint[Tail, UpdateInputByResult.type]
    ): Unit =
      summon[ToListConstraint[Head *: Tail, UpdateInputByResult.type]].convert(props)

