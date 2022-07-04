package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.ValidatedResult
import ElementQ.{*, given}
import Html.{*, given}
import Modifiers.{*, given}
import scala.language.implicitConversions

class ModelProps[M]:
  trait InputSpec[+E, +T]:
    val ele: HTMLElement
    def updateBy(model: Option[M]): Unit
    def validate(): ValidatedResult[E, T]

    def addClass(className: String): this.type =
      ele(cls := className)
      this

  trait DispSpec:
    val ele: HTMLElement
    def updateBy(model: Option[M]): Unit

  trait Prop[+E, +T]:
    def label: String
    def modelValue(m: M): T 
    lazy val inputSpec: InputSpec[E, T]
    lazy val dispSpec: DispSpec

  type Label[P] = P match {
    case Prop[e, t] => String
  }

  def label[P](p: P): Label[P] = p match {
    case pp: Prop[e, t] => pp.label
  }

  def labels(props: Tuple): Tuple.Map[props.type, Label] =
    props.map[Label]([T] => (t: T) => label(t))

  type Value[P] = P match {
    case Prop[e, t] => t
  }

  def value[P](p: P, m: M): Value[P] = p match {
    case pp: Prop[e, t] => pp.modelValue(m)
  }

  def values(props: Tuple, model: M): Tuple.Map[props.type, Value] =
    props.map[Value]([T] => (t: T) => value(t, model))

  type UpdateInput[P] = P match {
    case Prop[e, t] => Unit
  }

  def updateInput[P](p: P, modelOpt: Option[M]): UpdateInput[P] = p match {
    case pp: Prop[e, t] => pp.inputSpec.updateBy(modelOpt)
  }

  def updateInput(props: Tuple, modelOpt: Option[M]): Unit =
    props.map[UpdateInput]([T] => (t: T) => updateInput(t, modelOpt))
    
  type UpdateDisp[P] = P match {
    case Prop[e, t] => Unit
  }

  def updateDisp[P](p: P, modelOpt: Option[M]): UpdateDisp[P] = p match {
    case pp: Prop[e, t] => pp.dispSpec.updateBy(modelOpt)
  }

  def updateDisp(props: Tuple, modelOpt: Option[M]): Unit =
    props.map[UpdateDisp]([T] => (t: T) => updateDisp(t, modelOpt))

  case class LabelElement(label: String, element: HTMLElement)

  type FormPanel[P] = P match {
    case Prop[e, t] => (String, HTMLElement)
    case LabelElement => (String, HTMLElement)
  }

  def formPanel[P](p: P): FormPanel[P] = p match {
    case pp: Prop[e, t] => (pp.label, pp.inputSpec.ele)
    case pp: LabelElement => (pp.label, pp.element)
  }

  def formPanel(props: Tuple): HTMLElement =
    val dp = DispPanel(form = true)
    props.map[FormPanel]([T] => (t: T) => formPanel(t)).toList.foreach(pair =>
      val (label, ele) = pair.asInstanceOf[(String, HTMLElement)]
      dp.add(label, ele)  
    )
    dp.ele
    
  type DispPanel[P] = P match {
    case Prop[e, t] => (String, HTMLElement)
    case LabelElement => (String, HTMLElement)
  }

  def dispPanel[P](p: P): DispPanel[P] = p match {
    case pp: Prop[e, t] => (pp.label, pp.inputSpec.ele)
    case pp: LabelElement => (pp.label, pp.element)
  }

  def dispPanel(props: Tuple): HTMLElement =
    val dp = DispPanel()
    props.map[DispPanel]([T] => (t: T) => dispPanel(t)).toList.foreach(pair =>
      val (label, ele) = pair.asInstanceOf[(String, HTMLElement)]
      dp.add(label, ele)  
    )
    dp.ele
    
  type ResultOf[H] = H match {
    case Prop[e, t] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Prop[e, t] => p.inputSpec.validate()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))

