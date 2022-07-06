// package dev.myclinic.scala.web.appbase

// import dev.myclinic.scala.model.Shahokokuho
// import dev.myclinic.scala.model.ValidUpto
// import dev.myclinic.scala.web.appbase.ShahokokuhoValidator.{*, given}
// import dev.myclinic.scala.web.appbase.ShahokokuhoValidator
// import dev.fujiwara.kanjidate.KanjiDate
// import java.time.LocalDate
// import org.scalajs.dom.HTMLElement
// import dev.fujiwara.validator.section.Implicits.*
// import dev.fujiwara.domq.all.{*, given}
// import scala.language.implicitConversions
// import dev.fujiwara.domq.ModelProps
// import dev.myclinic.scala.util.ZenkakuUtil

// case class ShahokokuhoProps(var modelOpt: Option[Shahokokuho]) extends ModelProps[Shahokokuho] with PropUtil[Shahokokuho]:
//   private val validUptoChangedPublisher = new LocalEventPublisher[Option[LocalDate]]

//   val props = (
//     TextProp[HokenshaBangouError.type, Int](
//       "保険者番号",
//       _.hokenshaBangou,
//       HokenshaBangouValidator.validateInput
//     ).inputElementClass("hokensha-bangou-input"),
//     TextProp[HihokenshaKigouError.type, String](
//       "被保険者記号",
//       _.hihokenshaKigou,
//       HihokenshaKigouValidator.validate
//     ).inputElementClass("hihokensha-kigou-input"),
//     TextProp[HihokenshaBangouError.type, String](
//       "被保険者番号",
//       _.hihokenshaBangou,
//       HihokenshaBangouValidator.validate
//     ).inputElementClass("hihokensha-bangou-input"),
//     RadioProp[HonninError.type, Int](
//       "本人・家族",
//       List("本人" -> 1, "家族" -> 0),
//       0,
//       _.honninStore,
//       HonninValidator.validate
//     ),
//     DateProp[ValidFromError.type](
//       "期限開始",
//       _.validFrom,
//       ValidFromValidator.validateOption
//     ),
//     ValidUptoProp[ValidUptoError.type](
//       "期限終了",
//       _.validUpto,
//       ValidUptoValidator.validate,
//       suggest = suggestValidUpto
//     ),
//     RadioProp[KoureiError.type, Int](
//       "高齢",
//       RadioGroup.createItemsFromValues(ShahokokuhoValidator.validKoureiValues, formatKourei),
//       0,
//       _.koureiStore,
//       KoureiValidator.validate,
//       inputLayout = rg => 
//         val rest: List[Int] = rg.values.filter(_ != 0)
//         div(displayBlock,
//           div(cls := "upper-row", rg.getRadioLabel(0).ele),
//           div(cls := "lower-row",
//             rest.map(rg.getRadioLabel(_).ele)
//           )
//         )
//     ).inputElementClass("kourei-input"),
//     TextProp[EdabanError.type, String](
//       "枝番",
//       _.edaban,
//       EdabanValidator.validate
//     ).inputElementClass("edaban-input")
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
//     hokenshaBangouProp,
//     (
//       "記号・番号",
//       div(
//         displayBlock,
//         hihokenshaKigouProp.inputElement,
//         "・",
//         hihokenshaBangouProp.inputElement
//       )
//     ),
//     edabanProp,
//     koureiProp,
//     honninProp,
//     validFromProp,
//     validUptoProp
//   )

//   val dispProps = (
//     hokenshaBangouProp,
//     (
//       "記号・番号",
//       div(
//         displayBlock,
//         hihokenshaKigouProp.dispElement,
//         "・",
//         hihokenshaBangouProp.dispElement
//       )
//     ),
//     edabanProp,
//     koureiProp,
//     honninProp,
//     validFromProp,
//     validUptoProp
//   )

//   private def suggestValidUpto(): Option[LocalDate] = 
//     validFromProp.currentInputValue.map(_.plusYears(1).minusDays(1))

//   private def onValidUptoChange(dateOpt: Option[LocalDate]): Unit =
//     validUptoChangedPublisher.publish(dateOpt)

//   private def formatKourei(kourei: Int): String =
//     kourei match {
//       case 0 => "高齢でない"
//       case i => ZenkakuUtil.toZenkaku(s"${i}割")
//     }

//   def updateInput(): this.type = 
//     super.updateInput(props, modelOpt)
//     this

//   def updateDisp(): this.type = 
//     super.updateDisp(props, modelOpt)
//     this

//   def formPanel: HTMLElement =
//     super.formPanel(formProps)(cls := "shahokokuho-form")

//   def dispPanel: HTMLElement =
//     super.dispPanel(dispProps)(cls := "shahokokuho-disp")

//   def validatedForEnter(patientId: Int): Either[String, Shahokokuho] =
//     val rs = resultsOf(props)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateForEnter *:
//           PatientIdValidator.validate(patientId) *: rs
//       )
//       .asEither

//   def validatedForUpdate(): Either[String, Shahokokuho] =
//     val rs = resultsOf(props)
//     ShahokokuhoValidator
//       .validate(
//         ShahokokuhoIdValidator.validateOptionForUpdate(modelOpt.map(_.shahokokuhoId)) *:
//           PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
//       )
//       .asEither
