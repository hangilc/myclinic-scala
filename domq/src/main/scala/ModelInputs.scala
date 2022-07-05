package dev.fujiwara.domq

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.validator.section.ValidatedResult
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import dev.fujiwara.domq.dateinput.DateOptionInput
import dev.fujiwara.domq.dateinput.InitNoneConverter
import dev.myclinic.scala.model.ValidUpto

trait ModelInput[M]:
  trait Input[E, T]:
    val ele: HTMLElement
    def updateBy(m: Option[M]): Unit
    def validate(): ValidatedResult[E, T]

    def cssClass(className: String): this.type =
      ele(cls := className)
      this

trait ModelInputProcs[M]:
  this: ModelInput[M] =>
  
  private type Update[P] = P match {
    case Input[e, t] => Unit
  }

  private def update[P](p: P, modelOpt: Option[M]): Update[P] = p match {
    case pp: Input[e, t] => pp.updateBy(modelOpt)
  }

  def update(props: Tuple, modelOpt: Option[M]): Unit =
    props.map[Update]([T] => (t: T) => update(t, modelOpt))

  type ResultOf[H] = H match {
    case Input[e, t] => ValidatedResult[e, t]
  }

  def resultOf[T](t: T): ResultOf[T] =
    t match {
      case p: Input[e, t] => p.validate()
    }

  def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
    props.map[ResultOf]([T] => (t: T) => resultOf(t))

  private type Element[P] = P match {
    case Input[e, t] => HTMLElement
  }

  private def fElement[P](p: P): Element[P] = p match {
    case pp: Input[e, t] => pp.ele
  }

  def elements(inputs: Tuple): List[HTMLElement] =
    inputs.map([T] => (t: T) => fElement(t)).toList.map(_.asInstanceOf[HTMLElement])

  def createForm(props: Tuple, inputs: Tuple): HTMLElement =
    val panel = DispPanel(form = true)
    val labels: List[String] = ModelProp.labelsAsList(props)
    labels.zip(elements(inputs)).foreach {
      (l, e) => panel.add(l, e)
    }
    panel.ele

trait ModelInputs[M]:
  this: ModelInput[M] =>
  
  class TextInput[E, T](
    modelValue: M => String,
    validator: String => ValidatedResult[E, T],
    nonModelValue: String = ""
  ) extends Input[E, T]:
    val ele: HTMLInputElement = input
    def updateBy(modelOpt: Option[M]): Unit =
      ele.value = modelOpt.fold(nonModelValue)(modelValue(_))
    def validate(): ValidatedResult[E, T] =
      validator(ele.value)
      
  class RadioInput[E, T](
    modelValue: M => T,
    validator: T => ValidatedResult[E, T],
    data: List[(String, T)],
    initValue: T
  ) extends Input[E, T]:
    val radioGroup = RadioGroup[T](data, initValue = Some(initValue))
    val ele: HTMLElement = radioGroup.ele
    def updateBy(modelOpt: Option[M]): Unit =
      modelOpt.foreach(t => radioGroup.check(_))
    def validate(): ValidatedResult[E, T] =
      validator(radioGroup.selected)

  class DateInput[E](
    modelValue: M => LocalDate,
    validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
    initValue: Option[LocalDate],
    suggest: () => Option[LocalDate] = InitNoneConverter.defaultInitNoneFun
  ) extends Input[E, LocalDate]:
    given InitNoneConverter with 
      def convert: Option[LocalDate] = suggest()
    val dateInput = DateOptionInput(initValue)
    val ele: HTMLElement = dateInput.ele
    def updateBy(modelOpt: Option[M]): Unit =
      dateInput.init(modelOpt.map(modelValue(_)))
    def validate(): ValidatedResult[E, LocalDate] =
      validator(dateInput.value)

  class ValidUptoInput[E](
    modelValue: M => ValidUpto,
    validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
    initValue: Option[LocalDate]
  ) extends Input[E, ValidUpto]:
    val dateInput = DateOptionInput(initValue)
    val ele: HTMLElement = dateInput.ele
    def updateBy(modelOpt: Option[M]): Unit =
      dateInput.init(modelOpt.flatMap(m => modelValue(m).value))
    def validate(): ValidatedResult[E, ValidUpto] =
      validator(dateInput.value)