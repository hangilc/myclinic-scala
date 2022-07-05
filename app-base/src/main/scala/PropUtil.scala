package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.ModelProps
import scala.language.implicitConversions
import dev.fujiwara.validator.section.*
import dev.fujiwara.dateinput.DateOptionInput
import org.scalajs.dom.HTMLInputElement
import org.scalajs.dom.HTMLElement
import java.time.LocalDate
import dev.myclinic.scala.model.ValidUpto
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.dateinput.InitNoneConverter

trait PropUtil[M]:
  this: ModelProps[M] =>

  class TextInput[E, T](
      modelValue: M => T,
      validator: String => ValidatedResult[E, T],
      toInputValue: T => String,
      defaultValue: String
  ) extends InputSpec[E, T]:
    val input: HTMLInputElement = inputText
    val ele: HTMLElement = input
    def updateBy(model: Option[M]): Unit =
      input.value = model.fold(defaultValue)(m => toInputValue(modelValue(m)))
    def validate(): ValidatedResult[E, T] =
      validator(input.value)

  class RadioInput[E, T](
      radioGroup: RadioGroup[T],
      modelValue: M => T,
      validator: T => ValidatedResult[E, T],
      defaultValue: T
  ) extends InputSpec[E, T]:
    val ele: HTMLElement = radioGroup.ele
    def updateBy(model: Option[M]): Unit =
      radioGroup.check(model.fold(defaultValue)(modelValue(_)))
    def validate(): ValidatedResult[E, T] =
      validator(radioGroup.selected)

  class DateInput[E](
      modelValue: M => LocalDate,
      validator: Option[LocalDate] => ValidatedResult[E, LocalDate]
  )(using InitNoneConverter) extends InputSpec[E, LocalDate]:
    val dateInput = DateOptionInput()
    val ele: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.map(modelValue(_)))
    def validate(): ValidatedResult[E, LocalDate] =
      validator(dateInput.value)

  class ValidUptoInput[E](
      modelValue: M => ValidUpto,
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto]
  )(using InitNoneConverter) extends InputSpec[E, ValidUpto]:
    val dateInput = DateOptionInput()
    val ele: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.flatMap(modelValue(_).value))
    def validate(): ValidatedResult[E, ValidUpto] =
      validator(dateInput.value)

  class SpanDisp(
      modelValue: M => String,
      defaultValue: String
  ) extends DispSpec:
    val ele: HTMLElement = span
    def updateBy(model: Option[M]): Unit =
      ele(innerText := model.fold(defaultValue)(modelValue(_)))

  class ValidUptoDisp(
      modelValue: M => ValidUpto,
      defaultValue: String
  ) extends DispSpec:
    val ele: HTMLElement = span
    def updateBy(model: Option[M]): Unit =
      val t: String = model
        .flatMap(modelValue(_).value.map(d => KanjiDate.dateToKanji(d)))
        .getOrElse(defaultValue)
      ele(innerText := t)

  case class TextProp[E, T](
      val label: String,
      modelValueFun: M => T,
      validator: String => ValidatedResult[E, T],
      toInputValue: T => String = (t: T) => t.toString,
      toDispValue: T => String = (t: T) => t.toString,
      inputDefaultValue: String = "",
      dispDefaultValue: String = ""
  ) extends Prop[E, T]:
    def modelValue(m: M): T = modelValueFun(m)
    
    lazy val inputSpec = new TextInput[E, T](
      modelValue,
      validator,
      toInputValue,
      inputDefaultValue
    )
    lazy val dispSpec: DispSpec = new SpanDisp(
      m => toDispValue(modelValue(m)),
      dispDefaultValue
    )

  case class RadioProp[E, T](
      val label: String,
      data: List[(String, T)],
      defaultValue: T,
      modelValueFun: M => T,
      validator: T => ValidatedResult[E, T],
      dispDefaultValue: String = "",
      toDispValue: (T, RadioGroup[T]) => String = (t: T, g: RadioGroup[T]) =>
        g.findLabel(t),
      inputLayout: RadioGroup[T] => HTMLElement = RadioGroup.defaultLayout[T]
  ) extends Prop[E, T]:
    def modelValue(m: M): T = modelValueFun(m)
    
    val radioGroup =
      RadioGroup[T](data, initValue = Some(defaultValue), layout = inputLayout)
    lazy val inputSpec = new RadioInput[E, T](
      radioGroup,
      modelValue,
      validator,
      defaultValue
    )
    lazy val dispSpec = new SpanDisp(
      m => toDispValue(modelValue(m), radioGroup),
      dispDefaultValue
    )

  case class DateProp[E](
      val label: String,
      modelValueFun: M => LocalDate,
      validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
      dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
      dispDefaultValue: String = ""
  ) extends Prop[E, LocalDate]:
    lazy val inputSpec: DateInput[E] = new DateInput[E](
      modelValue,
      validator
    )
    def modelValue(m: M): LocalDate = modelValueFun(m)
    
    lazy val dispSpec = new SpanDisp(
      m => dateFormatter(modelValue(m)),
      dispDefaultValue
    )

    def currentInputValue: Option[LocalDate] = inputSpec.dateInput.value

  case class ValidUptoProp[E](
      val label: String,
      modelValueFun: M => ValidUpto,
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
      dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
      dispDefaultValue: String = "（期限なし）",
      suggest: () => Option[LocalDate] = () => None
  ) extends Prop[E, ValidUpto]:
    def modelValue(m: M): ValidUpto = modelValueFun(m)
    
    given InitNoneConverter with
      def convert: Option[LocalDate] = suggest()
    lazy val inputSpec: ValidUptoInput[E] = new ValidUptoInput[E](
      modelValue,
      validator
    )
    lazy val dispSpec = new SpanDisp(
      m => modelValue(m).value.map(dateFormatter).getOrElse(dispDefaultValue),
      dispDefaultValue
    )

    def onInputChange(handler: Option[LocalDate] => Unit): this.type = 
      inputSpec.dateInput.onChange(handler)
      this

    def currentInputValue: Option[LocalDate] =
      inputSpec.dateInput.value

// object PropUtilOrig:
//   class TextInput[M, E, T](
//       modelValue: M => T,
//       validator: String => ValidatedResult[E, T],
//       toInputValue: T => String,
//       defaultValue: String
//   ) extends InputSpec[M, E, T]:
//     val input: HTMLInputElement = inputText
//     val ele: HTMLElement = input
//     def updateBy(model: Option[M]): Unit =
//       input.value = model.fold(defaultValue)(m => toInputValue(modelValue(m)))
//     def validate(): ValidatedResult[E, T] =
//       validator(input.value)

//   class RadioInput[M, E, T](
//       radioGroup: RadioGroup[T],
//       modelValue: M => T,
//       validator: T => ValidatedResult[E, T],
//       defaultValue: T
//   ) extends InputSpec[M, E, T]:
//     val ele: HTMLElement = radioGroup.ele
//     def updateBy(model: Option[M]): Unit =
//       radioGroup.check(model.fold(defaultValue)(modelValue(_)))
//     def validate(): ValidatedResult[E, T] =
//       validator(radioGroup.selected)

//   class DateInput[M, E](
//       modelValue: M => LocalDate,
//       validator: Option[LocalDate] => ValidatedResult[E, LocalDate]
//   )(using InitNoneConverter) extends InputSpec[M, E, LocalDate]:
//     val dateInput = DateOptionInput()
//     val ele: HTMLElement = dateInput.ele
//     def updateBy(model: Option[M]): Unit =
//       dateInput.init(model.map(modelValue(_)))
//     def validate(): ValidatedResult[E, LocalDate] =
//       validator(dateInput.value)

//   class ValidUptoInput[M, E](
//       modelValue: M => ValidUpto,
//       validator: Option[LocalDate] => ValidatedResult[E, ValidUpto]
//   )(using InitNoneConverter) extends InputSpec[M, E, ValidUpto]:
//     val dateInput = DateOptionInput()
//     val ele: HTMLElement = dateInput.ele
//     def updateBy(model: Option[M]): Unit =
//       dateInput.init(model.flatMap(modelValue(_).value))
//     def validate(): ValidatedResult[E, ValidUpto] =
//       validator(dateInput.value)

//   class SpanDisp[M](
//       modelValue: M => String,
//       defaultValue: String
//   ) extends DispSpec[M]:
//     val ele: HTMLElement = span
//     def updateBy(model: Option[M]): Unit =
//       ele(innerText := model.fold(defaultValue)(modelValue(_)))

//   class ValidUptoDisp[M](
//       modelValue: M => ValidUpto,
//       defaultValue: String
//   ) extends DispSpec[M]:
//     val ele: HTMLElement = span
//     def updateBy(model: Option[M]): Unit =
//       val t: String = model
//         .flatMap(modelValue(_).value.map(d => KanjiDate.dateToKanji(d)))
//         .getOrElse(defaultValue)
//       ele(innerText := t)

//   case class TextProp[M, E, T](
//       val label: String,
//       modelValue: M => T,
//       validator: String => ValidatedResult[E, T],
//       toInputValue: T => String = (t: T) => t.toString,
//       toDispValue: T => String = (t: T) => t.toString,
//       inputDefaultValue: String = "",
//       dispDefaultValue: String = ""
//   ) extends Prop[M, E, T]:
//     lazy val inputSpec = new TextInput[M, E, T](
//       modelValue,
//       validator,
//       toInputValue,
//       inputDefaultValue
//     )
//     lazy val dispSpec: DispSpec[M] = new SpanDisp[M](
//       m => toDispValue(modelValue(m)),
//       dispDefaultValue
//     )

//   case class RadioProp[M, E, T](
//       val label: String,
//       data: List[(String, T)],
//       defaultValue: T,
//       modelValue: M => T,
//       validator: T => ValidatedResult[E, T],
//       dispDefaultValue: String = "",
//       toDispValue: (T, RadioGroup[T]) => String = (t: T, g: RadioGroup[T]) =>
//         g.findLabel(t),
//       inputLayout: RadioGroup[T] => HTMLElement = RadioGroup.defaultLayout[T]
//   ) extends Prop[M, E, T]:
//     val radioGroup =
//       RadioGroup[T](data, initValue = Some(defaultValue), layout = inputLayout)
//     lazy val inputSpec = new RadioInput[M, E, T](
//       radioGroup,
//       modelValue,
//       validator,
//       defaultValue
//     )
//     lazy val dispSpec = new SpanDisp[M](
//       m => toDispValue(modelValue(m), radioGroup),
//       dispDefaultValue
//     )

//   case class DateProp[M, E](
//       val label: String,
//       modelValue: M => LocalDate,
//       validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
//       dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
//       dispDefaultValue: String = ""
//   ) extends Prop[M, E, LocalDate]:
//     lazy val inputSpec: DateInput[M, E] = new DateInput[M, E](
//       modelValue,
//       validator
//     )
//     lazy val dispSpec = new SpanDisp[M](
//       m => dateFormatter(modelValue(m)),
//       dispDefaultValue
//     )

//     def currentInputValue: Option[LocalDate] = inputSpec.dateInput.value

//   case class ValidUptoProp[M, E](
//       val label: String,
//       modelValue: M => ValidUpto,
//       validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
//       dateFormatter: LocalDate => String = d => KanjiDate.dateToKanji(d),
//       dispDefaultValue: String = "（期限なし）",
//       suggest: () => Option[LocalDate] = () => None
//   ) extends Prop[M, E, ValidUpto]:
//     given InitNoneConverter with
//       def convert: Option[LocalDate] = suggest()
//     lazy val inputSpec: ValidUptoInput[M, E] = new ValidUptoInput[M, E](
//       modelValue,
//       validator
//     )
//     lazy val dispSpec = new SpanDisp[M](
//       m => modelValue(m).value.map(dateFormatter).getOrElse(dispDefaultValue),
//       dispDefaultValue
//     )

//     def onInputChange(handler: Option[LocalDate] => Unit): this.type = 
//       inputSpec.dateInput.onChange(handler)
//       this

//     def currentInputValue: Option[LocalDate] =
//       inputSpec.dateInput.value

