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

class ModelProp(val label: String)

trait PropElementProvider:
  def getProp: ModelProp
  def getElement: HTMLElement

trait DataValidator[E, T]:
  def validate(): ValidatedResult[E, T]

class ReadOnlyInput[E, T](cache: T, var validator: T => ValidatedResult[E, T])
    extends DataValidator[E, T]:
  def validate(): ValidatedResult[E, T] =
    validator(cache)

abstract class InputUI[T]:
  def getValue: T

class TextInputUI(initValue: String) extends InputUI[String]:
  val ele: HTMLInputElement = input
  ele.value = initValue
  def getValue(): String = ele.value

class RadioInputUI[T](
    data: List[(String, T)],
    initValue: T
) extends InputUI[T]:
  val radioGroup = RadioGroup[T](data, initValue = Some(initValue))
  lazy val ele: HTMLElement = radioGroup.ele
  def getValue(): T = radioGroup.selected

class DateInputUI(initValue: Option[LocalDate]) extends InputUI[Option[LocalDate]]:
  val dateInput = DateOptionInput(initValue)
  lazy val ele: HTMLElement = dateInput.ele
  def getValue(): Option[LocalDate] = dateInput.value

class ValidUptoInputUI(
    initValue: () => ValidUpto = () => ValidUpto(None)
) extends InputUI[ValidUpto]:
  val dateInput = DateOptionInput(initValue().value)
  lazy val ele: HTMLElement = dateInput.ele
  def getValue(): ValidUpto = ValidUpto(dateInput.value)

abstract case class BoundInput[M, I, E, T](
    prop: ModelProp,
    modelOption: Option[M],
    modelValue: M => T,
    defaultModelValue: () => T,
    toInputValue: T => I,
    validator: I => ValidatedResult[E, T]
) extends PropElementProvider with DataValidator[E, T]:
  val inputUI: InputUI[I]
  def getElement: HTMLElement = inputUI.ele
  def update(): Unit =
    val mval = modelOption.fold(defaultModelValue())(modelValue(_))
    inputUI.setValue(toInputValue(mval))
  def validate(): ValidatedResult[E, T] =
    val ival = inputUI.getValue()
    validator(ival)

case class LabelElement(label: String, element: HTMLElement)

trait BoundInputProcs extends ModelUtil:
  type PropElement[T] = T match {
    case PropElementProvider => (String, HTMLElement)
    case LabelElement        => (String, HTMLElement)
  }

  def fPropElement[T](t: T): PropElement[T] = t match {
    case tt: PropElementProvider => (tt.getProp.label, tt.getElement)
    case tt: LabelElement        => (tt.label, tt.element)
  }

  def propElements(inputs: Tuple): Tuple.Map[inputs.type, PropElement] =
    inputs.map([T] => (t: T) => fPropElement(t))

  type Update[I] = I match {
    case DataUpdator => Unit
  }

  def fUpdate[I](i: I): Update[I] = i match {
    case ii: DataUpdator => ii.update()
  }

  def update(inputs: Tuple): Unit =
    inputs.map([T] => (t: T) => fUpdate(t))

  type Validate[I] = I match {
    case DataValidator[e, t] => ValidatedResult[e, t]
  }

  def fValidate[I](i: I): Validate[I] = i match {
    case ii: DataValidator[e, t] => ii.validate()
  }

  def validate(inputs: Tuple): Tuple.Map[inputs.type, Validate] =
    inputs.map([T] => (t: T) => fValidate(t))

// abstract class BoundModelInput[M, E, I, T](
//     val prop: ModelProp,
//     mOpt: Option[M],
//     val ele: HTMLElement,
//     modelValue: M => T,
//     toInputValue: T => I,
//     defaultInputValue: () => I,
//     validator: I => ValidatedResult[E, T]
// ) extends ModelInput[E, T]:
//   def updateBy(i: I): Unit
//   def currentInputValue(): I
//   def update(): Unit =
//     updateBy(mOpt.fold(defaultInputValue())(m => toInputValue(modelValue(m))))
//   def validate(): ValidatedResult[E, T] =
//     validator(currentInputValue())

// trait ModelDisp:
//   val prop: ModelProp
//   def rep: String
//   def ele: HTMLElement
//   def update(): Unit

// class BoundModelDisp[M, T](
//   val prop: ModelProp,
//   mOpt: Option[M],
//   modelValue: M => T,
//   toRep: T => String = (i: T) => i.toString,
//   defaultRep: () => String = () => "",
//   val ele: HTMLElement = Html.span
// ) extends ModelDisp:
//   update()
//   def rep: String =
//     mOpt.fold(defaultRep())(m => toRep(modelValue(m)))
//   def update(): Unit = ele.innerText = rep

// trait BoundModelInputProcs extends ModelUtil:
//   type FormPanel[T] = T match {
//     case ModelInput[e, t]      => (String, HTMLElement)
//     case LabelElement => (String, HTMLElement)
//   }

//   def fFormPanel[T](t: T): FormPanel[T] = t match {
//     case tt: ModelInput[e, t]      => (tt.prop.label, tt.ele)
//     case tt: LabelElement => (tt.label, tt.element)
//   }

//   def formPanel(inputs: Tuple): HTMLElement =
//     val pairs = tupleToList[(String, HTMLElement)](
//       inputs.map([T] => (t: T) => fFormPanel(t))
//     )
//     val panel = DispPanel(form = true)
//     pairs.foreach(panel.add.tupled(_))
//     panel.ele

// case class Patient(patientId: Int, name: String, yomi: String)

// enum PatientProp(label: String) extends ModelProp(label):
//   case PatientId extends PatientProp("患者番号")
//   case NameProp extends PatientProp("名前")
//   case YomiProp extends PatientProp("よみ")

// object PatientProp:
//   val props = Tuple.fromArray(PatientProp.values)

// object ModelInputs:
//   class TextModelInput[M, E, T](
//       prop: ModelProp,
//       mOpt: Option[M],
//       modelValue: M => T,
//       validator: String => ValidatedResult[E, T],
//       toInputValue: T => String = (t: T) => t.toString,
//       defaultInputValue: () => String = () => "",
//       ele: HTMLInputElement = dev.fujiwara.domq.Html.input
//   ) extends BoundModelInput[M, E, String, T](
//         prop,
//         mOpt,
//         ele,
//         modelValue,
//         toInputValue,
//         defaultInputValue,
//         validator
//       ):
//     update()
//     def updateBy(t: String): Unit =
//       ele.value = t
//     def currentInputValue(): String =
//       ele.value

// class PatientInputs(modelOpt: Option[Patient]) extends BoundModelInputProcs with ModelUtil:
//   import ModelInputs.*
//   import PatientProp.*

//   object NameInput
//       extends TextModelInput[Patient, ValidatedResult[String, String], String](
//         NameProp,
//         modelOpt,
//         _.name,
//         s => cats.data.Validated.Valid(s)
//       )

//   extension (p: NameProp.type) def input: NameInput.type = NameInput

//   object YomiInput
//       extends TextModelInput[Patient, ValidatedResult[String, String], String](
//         YomiProp,
//         modelOpt,
//         _.yomi,
//         s => cats.data.Validated.Valid(s)
//       )

//   extension (p: YomiProp.type) def input: YomiInput.type = YomiInput

//   type CreateInput[P] = P match {
//     case NameProp.type => NameInput.type
//     case YomiProp.type => YomiInput.type
//   }

//   def fCreateInput[P](p: P): CreateInput[P] = p match {
//     case pp: NameProp.type => NameProp.input
//     case pp: YomiProp.type => YomiProp.input
//   }

//   def createInput(props: Tuple): Tuple.Map[props.type, CreateInput] =
//     props.map([T] => (t: T) => fCreateInput(t))

//   val formProps = (
//     NameProp,
//     YomiProp
//   )

//   def formPanel: HTMLElement = formPanel(formProps)
