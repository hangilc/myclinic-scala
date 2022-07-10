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
import dev.fujiwara.kanjidate.KanjiDate

trait LabelProvider:
  def getLabel: String

trait ValueProvider[T]:
  def getValue: T

trait DataGetter[M, T]:
  def getFrom(m: M): T

trait ElementProvider:
  def getElement: HTMLElement

trait DataValidator[E, T]:
  def validate(): ValidatedResult[E, T]

trait RepProvider:
  def getRep: String

trait InitValue[M, I]:
  def getInitValue(modelOpt: Option[M]): I

trait OnChangePublisher[T]:
  def onChange(handler: T => Unit): Unit

class ModelProp[M, T](label: String, getter: M => T)
    extends LabelProvider
    with DataGetter[M, T]:
  def getLabel: String = label
  def getFrom(m: M): T = getter(m)

object InitValue:
  def apply[M, I, T](
      getter: DataGetter[M, T],
      conv: T => I,
      defaultValue: I
  ): InitValue[M, I] =
    new InitValue[M, I]:
      def getInitValue(modelOpt: Option[M]): I =
        modelOpt.fold(defaultValue)(m => conv(getter.getFrom(m)))

object InitValueDynamic:
  def apply[M, I, T](
      getter: DataGetter[M, T],
      conv: T => I,
      defaultValue: () => I
  ): InitValue[M, I] =
    new InitValue[M, I]:
      def getInitValue(modelOpt: Option[M]): I =
        modelOpt.fold(defaultValue())(m => conv(getter.getFrom(m)))

class TextInput[T](initValue: T, toInputValue: T => String)
    extends ElementProvider
    with ValueProvider[String]:
  val ele: HTMLInputElement = inputText
  ele.value = toInputValue(initValue)
  def getElement: HTMLElement = ele
  def getValue: String = ele.value

class StringInput(initValue: String)
    extends TextInput[String](initValue, identity)

class RadioInput[T](initValue: T, data: List[(String, T)])
    extends ElementProvider
    with ValueProvider[T]:
  given RadioGroup.Layout[T] = layout
  val radioGroup = RadioGroup[T](data, initValue = Some(initValue))

  def layout: RadioGroup.Layout[T] = RadioGroup.defaultLayout[T]
  def getElement: HTMLElement = radioGroup.ele
  def getValue: T = radioGroup.selected

class DateInput(initValue: Option[LocalDate])(
  using dev.fujiwara.domq.dateinput.DateInput.Suggest
)
    extends ElementProvider
    with ValueProvider[Option[LocalDate]]:
  val dateInput = DateOptionInput(initValue)
  def getElement: HTMLElement = dateInput.ele
  def getValue: Option[LocalDate] = dateInput.value

class ValidUptoInput(initValue: ValidUpto)(
  using dev.fujiwara.domq.dateinput.DateInput.Suggest
)
    extends ElementProvider
    with ValueProvider[ValidUpto]
    with OnChangePublisher[ValidUpto]:
  val dateInput = DateOptionInput(
    initValue.value,
    formatNone = () => "（期限なし）",
    title = "有効期限の入力"
  )
  def getElement: HTMLElement = dateInput.ele
  def getValue: ValidUpto = ValidUpto(dateInput.value)
  def onChange(handler: ValidUpto => Unit): Unit =
    dateInput.onChange(dateOpt => handler(ValidUpto(dateOpt)))

class IntInput(initValue: Int)
    extends TextInput[Int](initValue, (i: Int) => i.toString)

class ModelPropRep[M, T](
    modelOpt: Option[M],
    prop: ModelProp[M, T],
    stringify: T => String = (t: T) => t.toString,
    defaultValue: String = ""
) extends RepProvider
    with LabelProvider
    with RepToSpan:
  def getRep: String =
    modelOpt.fold(defaultValue)(m => stringify(prop.getFrom(m)))
  def getLabel: String = prop.getLabel

class ModelDatePropRep[M](
    modelOpt: Option[M],
    prop: ModelProp[M, LocalDate],
    stringify: LocalDate => String = (t: LocalDate) => KanjiDate.dateToKanji(t),
    defaultValue: String = ""
) extends ModelPropRep[M, LocalDate](
      modelOpt,
      prop,
      stringify,
      defaultValue
    )

class ModelValidUptoPropRep[M](
    modelOpt: Option[M],
    prop: ModelProp[M, ValidUpto],
    stringify: ValidUpto => String = (t: ValidUpto) =>
      t.value.fold("（期限なし）")(d => KanjiDate.dateToKanji(d)),
    defaultValue: String = ""
) extends ModelPropRep[M, ValidUpto](
      modelOpt,
      prop,
      stringify,
      defaultValue
    )

trait RepToSpan extends ElementProvider:
  this: RepProvider =>

  def getElement: HTMLElement = span(innerText := getRep)

case class LabelElement(label: String, element: HTMLElement)
    extends LabelProvider
    with ElementProvider:
  def getLabel: String = label
  def getElement: HTMLElement = element

object ModelInputUtil:
  import ModelUtil.*

  type ElementPanel[T] = T match {
    case LabelProvider & ElementProvider => (String, HTMLElement)
  }

  def fElementPanel[T](t: T): ElementPanel[T] = t match {
    case tt: (LabelProvider & ElementProvider) => (tt.getLabel, tt.getElement)
  }

  def elementPanelTuple(tuple: Tuple): Tuple.Map[tuple.type, ElementPanel] =
    tuple.map([T] => (t: T) => fElementPanel(t))

  def elementPanelList(tuple: Tuple): List[(String, HTMLElement)] =
    tupleToList[(String, HTMLElement)](elementPanelTuple(tuple))

  def elementPanel(tuple: Tuple): HTMLElement =
    val panel = DispPanel()
    elementPanelList(tuple).foreach(panel.add.tupled(_))
    panel.ele

  type ResultsOf[I] = I match {
    case DataValidator[e, t] => ValidatedResult[e, t]
  }

  def fResultsOf[I](i: I): ResultsOf[I] = i match {
    case ii: DataValidator[e, t] => ii.validate()
  }

  def resultsOf(inputs: Tuple): Tuple.Map[inputs.type, ResultsOf] =
    inputs.map([T] => (t: T) => fResultsOf(t))

  type LabelRep[T] = T match {
    case LabelProvider & RepProvider => (String, String)
  }

  def fLabelRep[T](t: T): LabelRep[T] = t match {
    case tt: (LabelProvider & RepProvider) => (tt.getLabel, tt.getRep)
  }

  def labelRepTuple(tuple: Tuple): Tuple.Map[tuple.type, LabelRep] =
    tuple.map([T] => (t: T) => fLabelRep(t))

  def labelRep(tuple: Tuple): List[(String, String)] =
    tupleToList[(String, String)](labelRepTuple(tuple))
