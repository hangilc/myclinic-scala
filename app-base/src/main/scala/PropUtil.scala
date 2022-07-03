package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.validator.section.*
import dev.fujiwara.dateinput.DateOptionInput
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import dev.myclinic.scala.model.ValidUpto
import dev.fujiwara.kanjidate.KanjiDate

object PropUtil:
  class TextInput[M, E, T](
      modelValue: M => T,
      validator: String => ValidatedResult[E, T],
      toInputValue: T => String,
      defaultValue: String
  ) extends InputSpec[M, E, T]:
    val input: HTMLInputElement = inputText
    val ele: HTMLElement = input
    def updateBy(model: Option[M]): Unit =
      input.value = model.fold(defaultValue)(m => toInputValue(modelValue(m)))
    def validate(): ValidatedResult[E, T] =
      validator(input.value)

  class RadioInput[M, E, T](
      radioGroup: RadioGroup[T],
      modelValue: M => T,
      validator: T => ValidatedResult[E, T],
      defaultValue: T
  ) extends InputSpec[M, E, T]:
    val ele: HTMLElement = radioGroup.ele
    def updateBy(model: Option[M]): Unit =
      model.fold(defaultValue)(modelValue(_))
    def validate(): ValidatedResult[E, T] =
      validator(radioGroup.selected)

  class DateInput[M, E](
      modelValue: M => LocalDate,
      validator: Option[LocalDate] => ValidatedResult[E, LocalDate]
  ) extends InputSpec[M, E, LocalDate]:
    val dateInput = DateOptionInput()
    val ele: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.map(modelValue(_)))
    def validate(): ValidatedResult[E, LocalDate] =
      validator(dateInput.value)

  class ValidUptoInput[M, E](
      modelValue: M => ValidUpto,
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto]
  ) extends InputSpec[M, E, ValidUpto]:
    val dateInput = DateOptionInput()
    val ele: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.flatMap(modelValue(_).value))
    def validate(): ValidatedResult[E, ValidUpto] =
      validator(dateInput.value)

  class SpanDisp[M](
      modelValue: M => String,
      defaultValue: String
  ) extends DispSpec[M]:
    val ele: HTMLElement = span
    def updateBy(model: Option[M]): Unit =
      ele(innerText := model.fold(defaultValue)(modelValue(_)))

  class ValidUptoDisp[M](
      modelValue: M => ValidUpto,
      defaultValue: String
  ) extends DispSpec[M]:
    val ele: HTMLElement = span
    def updateBy(model: Option[M]): Unit =
      val t: String = model
        .flatMap(modelValue(_).value.map(d => KanjiDate.dateToKanji(d)))
        .getOrElse(defaultValue)
      ele(innerText := t)

  case class TextProp[M, E, T](
      val label: String,
      modelValue: M => T,
      validator: String => ValidatedResult[E, T],
      toInputValue: T => String = (t :T) => t.toString,
      toDispValue: T => String = (t :T) => t.toString,
      inputDefaultValue: String = "",
      dispDefaultValue: String = ""
  ) extends Prop[M, E, T]:
    lazy val inputSpec = new TextInput[M, E, T](
      modelValue,
      validator,
      toInputValue,
      inputDefaultValue
    )
    lazy val dispSpec: DispSpec[M] = new SpanDisp[M](
      m => toDispValue(modelValue(m)),
      dispDefaultValue
    )

  case class RadioProp[M, E, T](
    val label: String,
    data: List[(String, T)],
    defaultValue: T,
    modelValue: M => T,
    validator: T => ValidatedResult[E, T],
    dispDefaultValue: String = "",
    toDispValue: (T, RadioGroup[T]) => String = 
      (t: T, g: RadioGroup[T]) => g.findLabel(t)
  ) extends Prop[M, E, T]:
    val radioGroup = RadioGroup[T](data, initValue = Some(defaultValue))
    lazy val inputSpec = new RadioInput[M, E, T](
      radioGroup,
      modelValue,
      validator,
      defaultValue
    )
    lazy val dispSpec = new SpanDisp[M](
      m => toDispValue(modelValue(m), radioGroup),
      dispDefaultValue
    )

  case class DateProp[M, E](
    val label: String,
    modelValue: M => LocalDate,
    validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
    dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
    dispDefaultValue: String = ""
  ) extends Prop[M, E, LocalDate]:
    lazy val inputSpec = new DateInput[M, E](
      modelValue,
      validator
    )
    lazy val dispSpec = new SpanDisp[M](
      m => dateFormatter(modelValue(m)),
      dispDefaultValue
    )

  case class ValidUptoProp[M, E](
    val label: String,
    modelValue: M => ValidUpto,
    validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
    dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
    dispDefaultValue: String = "（期限なし）"
  ) extends Prop[M, E, ValidUpto]:
    lazy val inputSpec = new ValidUptoInput[M, E](
      modelValue,
      validator
    )
    lazy val dispSpec = new SpanDisp[M](
      m => dateFormatter(modelValue(m)),
      dispDefaultValue
    )


