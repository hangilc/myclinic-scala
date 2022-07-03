// package dev.myclinic.scala.web.reception.cashier

// import dev.fujiwara.domq.all.{*, given}
// import scala.language.implicitConversions
// import dev.myclinic.scala.model.*
// import dev.fujiwara.dateinput.DateOptionInput
// import dev.fujiwara.domq.DispPanel
// import dev.fujiwara.dateinput.InitNoneConverter
// import java.time.LocalDate
// import dev.myclinic.scala.web.appbase.ShahokokuhoValidator
// import dev.myclinic.scala.web.appbase.ShahokokuhoValidator.*
// import dev.myclinic.scala.web.appbase.formprop.Prop
// import dev.myclinic.scala.web.appbase.formprop.Prop.{*, given}
// import dev.fujiwara.validator.section.Implicits.*
// import org.scalajs.dom.HTMLElement

// case class ShahokokuhoForm(init: Option[Shahokokuho]):
//   val props = (
//     Prop(
//       "保険者番号",
//       () => input(cls := "hokensha-bangou-input"),
//       HokenshaBangouValidator.validateInput,
//       mRep[Shahokokuho, Int](_.hokenshaBangou)
//     ),
//     Prop(
//       "被保険者記号",
//       () => input(cls := "hihokensha-kigou-input"),
//       HihokenshaKigouValidator.validate,
//       mRep[Shahokokuho, String](_.hihokenshaKigou)
//     ),
//     Prop(
//       "被保険者番号",
//       () => input(cls := "hihokensha-bangou-input"),
//       HihokenshaBangouValidator.validate,
//       mRep[Shahokokuho, String](_.hihokenshaBangou)
//     ),
//     Prop.radio(
//       "本人・家族",
//       List("本人" -> 1, "家族" -> 0),
//       0,
//       HonninValidator.validate,
//       mRep[Shahokokuho, Int](_.honninStore)
//     ),
//     Prop.date(
//       "期限開始",
//       None,
//       ValidFromValidator.validateOption,
//       mRepDate[Shahokokuho](_.validFrom)
//     ),
//     Prop.validUpto(
//       "期限終了",
//       None,
//       ValidUptoValidator.validate,
//       mRepValidUpto[Shahokokuho](_.validUpto)
//     ),
//     Prop.radio(
//       "高齢",
//       List("高齢でない" -> 0, "１割" -> 1, "２割" -> 2, "３割" -> 3),
//       0,
//       KoureiValidator.validate,
//       mRep[Shahokokuho, Int](_.koureiStore, formatKourei _),
//       layout = (g: RadioGroup[Int]) => div(display := "block",
//         div(g.getRadioLabel(0).ele),
//         div(List(1, 2, 3).map(i => g.getRadioLabel(i).ele))
//       )
//     ),
//     Prop(
//       "枝番",
//       () => input(cls := "edaban"),
//       EdabanValidator.validate,
//       mRep[Shahokokuho, String](_.edaban)
//     )
//   )

//   val (
//     hokenshaBangouProp,
//     hihokenshaKigouProp,
//     hihokenshaBangouProp,
//     honninProp,
//     validFromProp,
//     validUptoProp,
//     koureiProp,
//     edabanProp
//   ) = props

//   val formProps = (
//     (
//       hokenshaBangouProp,
//       (
//         "記号・番号",
//         div(
//           hihokenshaKigouProp.elementCreator(),
//           "・",
//           hihokenshaBangouProp.elementCreator()
//         )
//       ),
//       edabanProp,
//       honninProp,
//       validFromProp,
//       validUptoProp,
//       koureiProp
//     )
//   )

//   val ele: HTMLElement = Prop.panel(formProps)(cls := "reception-shahokokuho-form")

//   val dispProps = (
//     hokenshaBangouProp,
//     hihokenshaKigouProp,
//     hihokenshaBangouProp,
//     edabanProp,
//     honninProp,
//     koureiProp,
//     validFromProp,
//     validUptoProp
//   )

//   def formatKourei(kourei: Int): String = kourei match {
//     case 0 => "高齢でない"
//     case 1 => "１割"
//     case 2 => "２割"
//     case 3 => "３割"
//     case _ => "不明"
//   }

//   def validateForEnter(patientId: Int): Either[String, Shahokokuho] =
//     val rs = Prop.resultsOf(props)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateForEnter
//           *: PatientIdValidator.validate(patientId)
//           *: rs
//       )
//       .asEither

//   def validateForUpdate: Either[String, Shahokokuho] =
//     val rs = Prop.resultsOf(props)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateOptionForUpdate(
//           init.map(_.shahokokuhoId)
//         )
//           *: PatientIdValidator.validateOption(init.map(_.patientId))
//           *: rs
//       )
//       .asEither

// //   import ShahokokuhoValidator.*
// //   ShahokokuhoValidator
// //     .validate(
// //       ShahokokuhoIdValidator.validateForEnter,
// //       PatientIdValidator.validate(patientId),
// //       HokenshaBangouValidator.validateInput(hokenshaBangouInput.value),
// //       HihokenshaKigouValidator.validate(hihokenshaKigouInput.value),
// //       HihokenshaBangouValidator.validate(hihokenshaBangouInput.value),
// //       HonninValidator.validate(honninInput.value),
// //       ValidFromValidator.validateOption(validFromInput.value),
// //       ValidUptoValidator.validate(validUptoInput.value),
// //       KoureiValidator.validate(koureiInput.value),
// //       EdabanValidator.validate(edabanInput.value)
// //     )
// //     .asEither

// // def validateForUpdate(patientId: Int): Either[String, Shahokokuho] =
// //   import ShahokokuhoValidator.*
// //   ShahokokuhoValidator
// //     .validate(
// //       ShahokokuhoIdValidator.validateOptionForUpdate(
// //         init.map(_.shahokokuhoId)
// //       ),
// //       PatientIdValidator.validate(patientId),
// //       HokenshaBangouValidator.validateInput(hokenshaBangouInput.value),
// //       HihokenshaKigouValidator.validate(hihokenshaKigouInput.value),
// //       HihokenshaBangouValidator.validate(hihokenshaBangouInput.value),
// //       HonninValidator.validate(honninInput.value),
// //       ValidFromValidator.validateOption(validFromInput.value),
// //       ValidUptoValidator.validate(validUptoInput.value),
// //       KoureiValidator.validate(koureiInput.value),
// //       EdabanValidator.validate(edabanInput.value)
// //     )
// //     .asEither

// // val hokenshaBangouInput = inputText()
// // val hihokenshaKigouInput = inputText()
// // val hihokenshaBangouInput = inputText()
// // val edabanInput = inputText()
// // val honninInput = RadioGroup[Int](
// //   List("本人" -> 1, "家族" -> 0),
// //   initValue = init.map(_.honninStore)
// // )
// // val koureiInput = RadioGroup[Int](
// //   List("高齢でない" -> 0, "１割" -> 1, "２割" -> 2, "３割" -> 3),
// //   initValue = init.map(_.koureiStore)
// // )
// // val validFromInput = DateOptionInput(init.map(_.validFrom))
// // val validUptoInput = DateOptionInput(
// //   init.flatMap(_.validUpto.value),
// //   formatNone = () => "（期限なし）"
// // )(
// //   using
// //   new InitNoneConverter:
// //     def convert: Option[LocalDate] =
// //       validFromInput.value.map(_.plusYears(1).minusDays(1))
// // )
// // val dp = DispPanel(form = true)
// // dp.ele(cls := "reception-shahokokuho-form")
// // dp.add(
// //   "保険者番号",
// //   hokenshaBangouInput(
// //     cls := "hokensha-bangou-input",
// //     value := initValue(_.hokenshaBangou.toString)
// //   )
// // )
// // dp.add(
// //   "記号・番号",
// //   div(
// //     hihokenshaKigouInput(
// //       placeholder := "記号",
// //       cls := "hihokensha-kigou-input",
// //       value := initValue(_.hihokenshaKigou)
// //     ),
// //     "・",
// //     hihokenshaBangouInput(
// //       placeholder := "番号",
// //       cls := "hihokensha-bangou-input",
// //       value := initValue(_.hihokenshaBangou)
// //     )
// //   )
// // )
// // dp.add("枝番", edabanInput(cls := "edaban"))
// // dp.add("本人・家族", honninInput.ele(cls := "honnin-input"))
// // dp.add("高齢", koureiInput.ele(cls := "kourei-input"))
// // dp.add("期限開始", validFromInput.ele(cls := "valid-from-input"))
// // dp.add("期限終了", validUptoInput.ele(cls := "valid-upto-input"))

// // def ele = dp.ele

// // def initValue(f: Shahokokuho => String): String =
// //   init.map(f).getOrElse("")
