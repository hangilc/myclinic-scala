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
  case class TextInput[M, E, T](
      modelValue: M => String,
      validator: String => ValidatedResult[E, T],
      noneValue: String = ""
  ) extends InputSpec[M, E, T]:
    def createElement(): HTMLElement = element
    def updateBy(model: Option[M]): Unit =
      element.value = model.fold(noneValue)(modelValue(_))
    def validate: ValidatedResult[E, T] =
      validator(element.value)

  case class RadioInput[M, E, T](
      data: List[(String, T)],
      init: T,
      modelValue: M => T,
      validator: T => ValidatedResult[E, T],
      postCreate: HTMLElement => Unit = _ => ()
  ) extends InputSpec[M, E, T]:
    lazy val radioGroup =
      val r = RadioGroup[T](data, initValue = Some(init))
      postCreate(r.ele)
      r

    def createElement: HTMLElement = radioGroup.ele
    def updateBy(model: Option[M]): Unit =
      model.fold(init)(modelValue(_))
    def validate: ValidatedResult[E, T] =
      validator(radioGroup.selected)

  case class DateInput[M, E, T](
      modelValue: M => LocalDate,
      validator: Option[LocalDate] => ValidatedResult[E, T],
      postCreate: HTMLElement => Unit = _ => ()
  ) extends InputSpec[M, E, T]:
    lazy val dateInput =
      val di = DateOptionInput()
      postCreate(di.ele)
      di
    def createElement: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.map(modelValue(_)))
    def validate: ValidatedResult[E, T] =
      validator(dateInput.value)

  case class ValidUptoInput[M, E](
      modelValue: M => ValidUpto,
      validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
      postCreate: HTMLElement => Unit = _ => ()
  ) extends InputSpec[M, E, ValidUpto]:
    lazy val dateInput =
      val di = DateOptionInput()
      postCreate(di.ele)
      di

    def createElement: HTMLElement = dateInput.ele
    def updateBy(model: Option[M]): Unit =
      dateInput.init(model.flatMap(modelValue(_).value))
    def validate: ValidatedResult[E, ValidUpto] =
      validator(dateInput.value)

  case class SpanDisp[M](
      modelValue: M => String,
      postCreate: HTMLElement => Unit = _ => (),
      noneValue: String = ""
  ) extends DispSpec[M]:
    lazy val element: HTMLElement =
      val e = span
      postCreate(e)
      e

    def createElement: HTMLElement = element
    def updateBy(model: Option[M]): Unit =
      element(innerText := model.fold(noneValue)(modelValue(_)))

  case class ValidUptoDisp[M](
      modelValue: M => ValidUpto,
      postCreate: HTMLElement => Unit = _ => (),
      noneValue: String = "（期限なし）"
  ) extends DispSpec[M]:
    lazy val element: HTMLElement =
      val e = span
      postCreate(e)
      e
    def createElement: HTMLElement = element
    def updateBy(model: Option[M]): Unit =
      val t: String = model
        .flatMap(modelValue(_).value.map(d => KanjiDate.dateToKanji(d)))
        .getOrElse(noneValue)
      element(innerText := t)



