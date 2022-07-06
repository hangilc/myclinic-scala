package dev.fujiwara.domq

import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.ValidatedResult
import org.scalajs.dom.HTMLInputElement

class ModelProp(val label: String)

trait ModelInput[E, T]:
  val prop: ModelProp
  val ele: HTMLElement
  def update(): Unit
  def validate(): ValidatedResult[E, T]

abstract class BoundModelInput[M, E, I, T](
    val prop: ModelProp,
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
    updateBy(mOpt.fold(defaultInputValue())(m => toInputValue(modelValue(m))))
  def validate(): ValidatedResult[E, T] =
    validator(currentInputValue())

trait BoundModelInputProcs extends ModelPropUtil:
  type FormPanel[T] = T match {
    case ModelInput[e, t]      => (String, HTMLElement)
    case (String, HTMLElement) => (String, HTMLElement)
  }

  def fFormPanel[T](t: T): FormPanel[T] = t match {
    case tt: ModelInput[e, t]      => (tt.prop.label, tt.ele)
    case tt: (String, HTMLElement) => tt
  }

  def formPanel(inputs: Tuple): HTMLElement =
    val pairs = tupleToList[(String, HTMLElement)](
      inputs.map([T] => (t: T) => fFormPanel(t))
    )
    val panel = DispPanel(form = true)
    pairs.foreach(panel.add.tupled(_))
    panel.ele

case class Patient(patientId: Int, name: String, yomi: String)

enum PatientProp(label: String) extends ModelProp(label):
  case PatientId extends PatientProp("患者番号")
  case NameProp extends PatientProp("名前")
  case YomiProp extends PatientProp("よみ")

object PatientProp:
  val props = Tuple.fromArray(PatientProp.values)

object ModelInputs:
  class TextModelInput[M, E, T](
      prop: ModelProp,
      mOpt: Option[M],
      modelValue: M => T,
      validator: String => ValidatedResult[E, T],
      toInputValue: T => String = (t: T) => t.toString,
      defaultInputValue: () => String = () => "",
      ele: HTMLInputElement = dev.fujiwara.domq.Html.input
  ) extends BoundModelInput[M, E, String, T](
        prop,
        mOpt,
        ele,
        modelValue,
        toInputValue,
        defaultInputValue,
        validator
      ):
    update()
    def updateBy(t: String): Unit =
      ele.value = t
    def currentInputValue(): String =
      ele.value

class PatientInputs(modelOpt: Option[Patient]) extends BoundModelInputProcs with ModelPropUtil:
  import ModelInputs.*
  import PatientProp.*

  object NameInput
      extends TextModelInput[Patient, ValidatedResult[String, String], String](
        NameProp,
        modelOpt,
        _.name,
        s => cats.data.Validated.Valid(s)
      )

  extension (p: NameProp.type) def input: NameInput.type = NameInput

  object YomiInput
      extends TextModelInput[Patient, ValidatedResult[String, String], String](
        YomiProp,
        modelOpt,
        _.yomi,
        s => cats.data.Validated.Valid(s)
      )

  extension (p: YomiProp.type) def input: YomiInput.type = YomiInput

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

  def formPanel: HTMLElement = formPanel(formProps)
