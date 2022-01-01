// package dev.myclinic.scala.web.appbase

// import dev.fujiwara.domq.ElementQ.{*, given}
// import dev.fujiwara.domq.Html.{*, given}
// import dev.fujiwara.domq.Modifiers.{*, given}
// import dev.fujiwara.domq.{Modifier}
// import scala.language.implicitConversions
// import cats.data.ValidatedNec
// import cats.implicits.*
// import cats.data.Validated.{validNec, invalidNec, condNec}
// import java.time.LocalDate
// import cats.data.Validated.Valid
// import cats.data.Validated.Invalid
// import dev.myclinic.scala.util.KanjiDate
// import dev.myclinic.scala.validator.DateValidator
// import scala.util.Success
// import scala.util.Try
// import scala.util.Failure
// import org.scalajs.dom.{HTMLElement}

// class DateInput(gengouList: List[KanjiDate.Gengou] = KanjiDate.Gengou.list):
//   val eGengouSelect: HTMLElement = select(
//     gengouList.map(g => (option(g.name, attr("value") := g.name): Modifier)): _*
//   )
//   val eNenInput = inputText()
//   val eMonthInput = inputText()
//   val eDayInput = inputText()
//   val ele =
//     div(cls := "appbase-date-input")(
//       eGengouSelect(cls := "gengou"),
//       eNenInput(cls := "nen"),
//       span("年", cls := "label"),
//       eMonthInput(cls := "month"),
//       span("月", cls := "label"),
//       eDayInput(cls := "day"),
//       span("日", cls := "label")
//     )

//   def selectGengou(gengou: KanjiDate.Gengou): Unit =
//     eGengouSelect.setSelectValue(gengou.name)

//   def validate(): DateValidator.Result[LocalDate] =
//     DateValidator
//       .validateDate(
//         DateValidator.validateGengouInput(
//           eGengouSelect.getSelectValue
//         ),
//         DateValidator.validateNenInput(eNenInput.value),
//         DateValidator.validateMonthInput(eMonthInput.value),
//         DateValidator.validateDayInput(eDayInput.value)
//       )

