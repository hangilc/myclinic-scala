// package dev.fujiwara.domq

// import dev.fujiwara.domq.all.{*, given}
// import scala.language.implicitConversions
// import org.scalajs.dom.HTMLInputElement
// import dev.fujiwara.validator.section.ValidatedResult
// import org.scalajs.dom.HTMLElement
// import java.time.LocalDate
// import dev.fujiwara.domq.dateinput.DateOptionInput
// import dev.myclinic.scala.model.ValidUpto
// import dev.fujiwara.domq.dateinput.DateInput

// trait ModelInput[E, T]:
//   val ele: HTMLElement
//   def updateBy(v: T): Unit
//   def validate(): ValidatedResult[E, T]

// trait BoundModelInput[M, E, T](model: M, input: ModelInput[E, T]) extends ModelInput[E, T]:
//   export input.*
//   def modelValue(m: M): T
//   def update(): Unit = updateBy(modelValue(model))

// trait ModelInputProcs extends ModelPropUtil:

//   def createInputs(props: NonEmptyTuple): NonEmptyTuple

//   private type Update[P] = P match {
//     case BoundModelInput[_, e, t] => Unit
//   }

//   private def update[P](p: P): Update[P] = p match {
//     case pp: BoundModelInput[_, e, t] => pp.update()
//   }

//   def update(boundInputs: Tuple): Unit =
//     boundInputs.map[Update]([T] => (t: T) => update(t))

//   type ResultOf[H] = H match {
//     case BoundModelInput[_, e, t] => ValidatedResult[e, t]
//   }

//   def resultOf[T](t: T): ResultOf[T] =
//     t match {
//       case p: BoundModelInput[_, e, t] => p.validate()
//     }

//   def resultsOf(props: Tuple): Tuple.Map[props.type, ResultOf] =
//     props.map[ResultOf]([T] => (t: T) => resultOf(t))


//   type Element[P] = P match {
//     case BoundModelInput[_, e, t] => HTMLElement
//   }

//   def fElement[P](p: P): Element[P] = p match {
//     case pp: BoundModelInput[_, e, t] => pp.ele
//   }

//   def elementsOf(inputs: Tuple): Tuple.Map[inputs.type, Element] =
//     inputs.map([T] => (t: T) => fElement(t))

//   def elementListOf(inputs: Tuple): List[HTMLElement] =
//     tupleToList[HTMLElement](inputs.map([T] => (t: T) => fElement(t)))

//   type ElementFromProp[P] = P match {
//     case ModelProp => 
//   }

//   def elementsFromProps(props: Tuple): 

//   // def createForm(props: Tuple, inputs: Tuple): HTMLElement =
//   //   val ls = ModelProp.labelsAsList(props)
//   //   val es = elements(inputs)
//   //   val panel = DispPanel(form = true)
//   //   ls.zip(es).foreach {
//   //     (label, element) => panel.add(label, element)
//   //   }
//   //   panel.ele

// trait ModelInputs[M]:
//   this: ModelInput[M] =>

//   class ModelTextInput[E, T](
//       modelValue: M => String,
//       validator: String => ValidatedResult[E, T],
//       noneModelValue: String = ""
//   ) extends Input[E, T]:
//     val ele: HTMLInputElement = input
//     def updateBy(modelOpt: Option[M]): Unit =
//       ele.value = modelOpt.fold(noneModelValue)(modelValue(_))
//     def validate(): ValidatedResult[E, T] =
//       validator(ele.value)

//   class ModelRadioInput[E, T](
//       modelValue: M => T,
//       validator: T => ValidatedResult[E, T],
//       data: List[(String, T)],
//       initValue: T
//   ) extends Input[E, T]:
//     val radioGroup = RadioGroup[T](data, initValue = Some(initValue))
//     val ele: HTMLElement = radioGroup.ele
//     def updateBy(modelOpt: Option[M]): Unit =
//       modelOpt.foreach(t => radioGroup.check(_))
//     def validate(): ValidatedResult[E, T] =
//       validator(radioGroup.selected)

//   class ModelDateInput[E](
//       modelValue: M => LocalDate,
//       validator: Option[LocalDate] => ValidatedResult[E, LocalDate],
//       initValue: Option[LocalDate]
//   )(using DateInput.Suggest) extends Input[E, LocalDate]:
//     val dateInput = DateOptionInput(initValue)
//     val ele: HTMLElement = dateInput.ele
//     def updateBy(modelOpt: Option[M]): Unit =
//       dateInput.init(modelOpt.map(modelValue(_)))
//     def validate(): ValidatedResult[E, LocalDate] =
//       validator(dateInput.value)

//   class ModelValidUptoInput[E](
//       modelValue: M => ValidUpto,
//       validator: Option[LocalDate] => ValidatedResult[E, ValidUpto],
//       initValue: Option[LocalDate]
//   )(using DateInput.Suggest) extends Input[E, ValidUpto]:
//     val dateInput = DateOptionInput(initValue)
//     val ele: HTMLElement = dateInput.ele
//     def updateBy(modelOpt: Option[M]): Unit =
//       dateInput.init(modelOpt.flatMap(m => modelValue(m).value))
//     def validate(): ValidatedResult[E, ValidUpto] =
//       validator(dateInput.value)
