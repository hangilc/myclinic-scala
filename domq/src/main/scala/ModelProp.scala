package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.ValidatedResult
import org.scalajs.dom.HTMLInputElement

class ModelProp(val label: String)

trait ModelInput[E, T]:
  val ele: HTMLElement
  def update(): Unit
  def validate(): ValidatedResult[E, T]

abstract class BoundModelInput[M, E, I, T](
    mOpt: Option[M],
    val ele: HTMLElement,
    modelValue: M => T,
    toInputValue: T => I,
    defaultInputValue: () => I,
    validator: I => ValidatedResult[E, T]
) extends ModelInput[E, T]:
  def updateBy(i: I): Unit
  def currentInputValue(): I
  def update(): Unit =
    updateBy(mOpt.fold(defaultInputValue())(m => toInputValue(m)))
  def validate(): ValidatedResult[E, T] =
    validator(currentInputValue())

case class Patient(patientId: Int, name: String, yomi: String)

enum PatientProp(label: String) extends ModelProp(label):
  case PatientId extends PatientProp("患者番号")
  case NameProp extends PatientProp("名前")
  case YomiProp extends PatientProp("よみ")

object PatientProp:
  val props = Tuple.fromArray(PatientProp.values)

object ModelInputs:
  class TextModelInput[M, E, T](
    modelValue: M => T,
    validator: String => ValidatedResult[E, T],
    var mOpt: Option[M] = None,
    toInputValue: T => String = (t: T) => t.toString,
    defaultInputValue: () => String = () => "",
    ele: HTMLInputElement = dev.fujiwara.domq.Html.input,
  ) extends BoundModelInput[M, E, String, T](
    mOpt,
    ele,
    modelValue,
    toInputValue,
    defaultInputValue,
    validator
  ):
    def updateBy(t: String): Unit = 
      ele.value = t
    def currentInputValue(): String =
      ele.value
    def bind(newModel: Option[M]): this.type =
      mOpt = newModel
      update()
      this

object PatientInputs extends ModelPropUtil:
  import ModelInputs.*
  import PatientProp.*

  trait PatientInput

  object NameInput extends TextModelInput[Patient, ValidatedResult[String, String], String](
    _.name,
    s => cats.data.Validated.Valid(s)
  ) with PatientInput

  extension (p: NameProp.type)
    def input: NameInput.type = NameInput

  object YomiInput extends TextModelInput[Patient, ValidatedResult[String, String], String](
    _.yomi,
    s => cats.data.Validated.Valid(s)
  ) with PatientInput

  extension (p: YomiProp.type)
    def input: YomiInput.type = YomiInput

  type CreateInput[P] = P match {
    case NameProp.type => NameInput.type
    case YomiProp.type => YomiInput.type
  }

  def fCreateInput[P](p: P): CreateInput[P] = p match {
    case pp: NameProp.type => NameProp.input
    case pp: YomiProp.type => YomiProp.input
  }

  def createInput(props: Tuple): Tuple.Map[props.type, CreateInput] =
    props.map([T] => (t: T) => fCreateInput(t))

  val formProps = (
    NameProp,
    YomiProp
  )


