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

object PropUtil:
  case class TextInput[M, E, T](
    modelValue: M => String,
    validator: String => ValidatedResult[E, T],
    postCreate: HTMLInputElement => Unit = _ => (),
    noneValue: String = ""
  ) extends InputSpec[M, E, T]:
    lazy val element: HTMLInputElement = 
      val e = inputText
      postCreate(e)
      e
    def createElement: HTMLElement = element
    def updateBy(model: Option[M]): Unit =
      element.value = model.fold(noneValue)(modelValue(_))
    def validate: ValidatedResult[E, T] =
      validator(element.value)

  case class RadioInput[M, E, T](
    data: List[(String, T)],
    init: T,
    modelValue: M => T,
    validator: T => ValidatedResult[E, T],
    postCreate: HTMLElement => Unit
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
    postCreate: HTMLElement => Unit
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
    postCreate: HTMLElement => Unit
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
    def updateBy(model: Option[M]) =
      element(innerText := model.fold(noneValue)(modelValue(_)))
  




  import dev.myclinic.scala.model.Patient
  import dev.myclinic.scala.web.appbase.PatientValidator.{*, given}
  import dev.myclinic.scala.web.appbase.PatientValidator

  val patientProps = (
    Prop[Patient, LastNameError.type, String](
      "姓",
      TextInput(
        _.lastName,
        LastNameValidator.validate
      ),
      SpanDisp(
        _.lastName
      )
    ),
    Prop[Patient, FirstNameError.type, String](
      "名",
      TextInput(
        _.firstName,
        FirstNameValidator.validate
      ),
      SpanDisp(
        _.firstName
      )
    ),
  )
  val ele = Prop.formPanel(patientProps)
  val inputUpdater = new InputUpdater[Patient](Some(patient))
  import inputUpdater.given
  inputUpdater.updateInput(patientProps)

