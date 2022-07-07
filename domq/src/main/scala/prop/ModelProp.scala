package dev.fujiwara.domq.prop

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.ValidatedResult
import org.scalajs.dom.HTMLInputElement
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.*
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.RadioGroup
import java.time.LocalDate
import dev.fujiwara.domq.dateinput.DateOptionInput
import dev.fujiwara.domq.dateinput.DateInput
import dev.myclinic.scala.model.ValidUpto

trait LabelProvider:
  def getLabel: String

trait DataProvider[T]:
  def getValue: T

trait DataGetter[M, T]:
  def getFrom(m: M): T

trait ElementProvider:
  def getElement: HTMLElement

trait ValidationProvider[E, T]:
  def validate(): ValidatedResult[E, T]

trait DispProvider:
  def disp: String

trait LabelElementProvider extends LabelProvider with ElementProvider

trait LabelDispProvider extends LabelProvider with DispProvider

class ModelProp(label: String) extends LabelProvider:
  def getLabel: String = label

class InitValue[M, T](getter: DataGetter[M, T], defaultValue: T):
  def getInitValue(modelOpt: Option[M]): T =
    modelOpt.fold(defaultValue)(m => getter.getFrom(m))

class TextInput[T](initValue: T, toInputValue: T => String)
    extends ElementProvider
    with DataProvider[String]:
  val ele: HTMLInputElement = input
  ele.value = toInputValue(initValue)
  def getElement: HTMLElement = ele
  def getValue: String = ele.value

class RadioInput[T](initValue: T, data: List[(String, T)])
    extends ElementProvider
    with DataProvider[T]:
  val radioGroup = RadioGroup[T](data, initValue = Some(initValue))
  def getElement: HTMLElement = radioGroup.ele
  def getValue: T = radioGroup.selected

class DateInput(initValue: Option[LocalDate])
    extends ElementProvider
    with DataProvider[Option[LocalDate]]:
  val dateInput = DateOptionInput(initValue)
  def getElement: HTMLElement = dateInput.ele
  def getValue: Option[LocalDate] = dateInput.value

class ValidUptoInput(initValue: ValidUpto)
    extends ElementProvider
    with DataProvider[ValidUpto]:
  val dateInput = DateOptionInput(initValue.value)
  def getElement: HTMLElement = dateInput.ele
  def getValue: ValidUpto = ValidUpto(dateInput.value)

class StringInput(initValue: String)
    extends TextInput[String](initValue, identity)

class IntInput(initValue: Int)
    extends TextInput[Int](initValue, (i: Int) => i.toString)

class ModelPropDisp[M, T](
    modelOpt: Option[M],
    init: InitValue[M, T],
    stringify: T => String = (t: T) => t.toString
) extends DispProvider:
  def disp: String = stringify(init.getInitValue(modelOpt))

object ModelProp:
  import ModelUtil.*

  type ElementPanel[T] = T match {
    case LabelElementProvider => (String, HTMLElement)
  }

  def fElementPanel[T](t: T): ElementPanel[T] = t match {
    case tt: LabelElementProvider => (tt.getLabel, tt.getElement)
  }

  def elementPanelTuple(tuple: Tuple): Tuple.Map[tuple.type, ElementPanel] =
    tuple.map([T] => (t: T) => fElementPanel(t))

  def elementPanelList(tuple: Tuple): List[(String, HTMLElement)] =
    tupleToList[(String, HTMLElement)](elementPanelTuple(tuple))

  def elementPanel(tuple: Tuple): HTMLElement =
    val panel = DispPanel()
    elementPanelList(tuple).foreach(panel.add.tupled(_))
    panel.ele

// trait PropElementProvider:
//   def getProp: ModelProp
//   def getElement: HTMLElement

// trait DataValidator[E, T]:
//   def validate(): ValidatedResult[E, T]

// class ReadOnlyInput[E, T](cache: T, validator: T => ValidatedResult[E, T])
//     extends DataValidator[E, T]:
//   def validate(): ValidatedResult[E, T] =
//     validator(cache)

// abstract class InputUI[T]:
//   def getElement: HTMLElement
//   def getValue: T

// class TextInputUI(initValue: String) extends InputUI[String]:
//   val ele: HTMLInputElement = input
//   ele.value = initValue
//   def getElement: HTMLElement = ele
//   def getValue: String = ele.value

// class RadioInputUI[T](
//     data: List[(String, T)],
//     initValue: T
// ) extends InputUI[T]:
//   val radioGroup = RadioGroup[T](data, initValue = Some(initValue))
//   def getElement: HTMLElement = radioGroup.ele
//   def getValue: T = radioGroup.selected

// class DateOptionInputUI(initValue: Option[LocalDate]) extends InputUI[Option[LocalDate]]:
//   val dateInput = DateOptionInput(initValue)
//   def getElement: HTMLElement = dateInput.ele
//   def getValue: Option[LocalDate] = dateInput.value

// class ValidUptoInputUI(
//     initValue: ValidUpto
// ) extends InputUI[ValidUpto]:
//   val dateInput = DateOptionInput(initValue.value)
//   def getElement: HTMLElement = dateInput.ele
//   def getValue: ValidUpto = ValidUpto(dateInput.value)

// abstract case class BoundInput[M, I, E, T](
//     prop: ModelProp,
//     modelOption: Option[M],
//     modelInputValue: M => I,
//     defaultInputValue: () => I,
//     validator: I => ValidatedResult[E, T]
// ) extends PropElementProvider with DataValidator[E, T]:
//   val inputUI: InputUI[I]
//   def resolveInitValue(): I =
//     modelOption.fold(defaultInputValue())(modelInputValue(_))
//   def getElement: HTMLElement = inputUI.getElement
//   def validate(): ValidatedResult[E, T] =
//     val ival = inputUI.getValue
//     validator(ival)

// case class LabelElement(label: String, element: HTMLElement)

// trait BoundInputProcs extends ModelUtil:
//   type PropElement[T] = T match {
//     case PropElementProvider => (String, HTMLElement)
//     case LabelElement        => (String, HTMLElement)
//   }

//   def fPropElement[T](t: T): PropElement[T] = t match {
//     case tt: PropElementProvider => (tt.getProp.label, tt.getElement)
//     case tt: LabelElement        => (tt.label, tt.element)
//   }

//   def propElements(inputs: Tuple): Tuple.Map[inputs.type, PropElement] =
//     inputs.map([T] => (t: T) => fPropElement(t))

//   def propElementsAsList(inputs: Tuple): List[(String, HTMLElement)] =
//     tupleToList[(String, HTMLElement)](propElements(inputs))

//   def formPanel(inputs: Tuple): HTMLElement =
//     val panel = DispPanel(form = true)
//     propElementsAsList(inputs).foreach(panel.add.tupled(_))
//     panel.ele

//   type ResultsOf[I] = I match {
//     case DataValidator[e, t] => ValidatedResult[e, t]
//   }

//   def fResultsOf[I](i: I): ResultsOf[I] = i match {
//     case ii: DataValidator[e, t] => ii.validate()
//   }

//   def resultsOf(inputs: Tuple): Tuple.Map[inputs.type, ResultsOf] =
//     inputs.map([T] => (t: T) => fResultsOf(t))
