// package dev.myclinic.scala.web.reception.cashier

// import dev.fujiwara.domq.all.{*, given}
// import scala.language.implicitConversions
// import dev.myclinic.scala.model.*
// import dev.fujiwara.dateinput.DateOptionInput
// import dev.fujiwara.domq.DispPanel
// import dev.fujiwara.dateinput.InitNoneConverter
// import java.time.LocalDate
// import dev.myclinic.scala.web.appbase.KoukikoureiValidator
// import dev.myclinic.scala.web.appbase.KoukikoureiValidator.*
// import org.scalajs.dom.HTMLElement
// import org.scalajs.dom.HTMLInputElement
// import dev.myclinic.scala.web.appbase.formprop.Prop
// import dev.myclinic.scala.web.appbase.formprop.Prop.{*, given}

// class KoukikoureiForm(init: Option[Koukikourei]):

//   val futanWariData = List(
//     "１割" -> 1,
//     "２割" -> 2,
//     "３割" -> 3
//   )

//   val props = (
//     Prop(
//       "保険者番号",
//       () => input,
//       HokenshaBangouValidator.validate,
//       mRep[Koukikourei, String](_.hokenshaBangou)
//     ),
//     Prop(
//       "被保険者番号",
//       () => input,
//       HihokenshaBangouValidator.validate,
//       mRep[Koukikourei, String](_.hihokenshaBangou)
//     ),
//     Prop.radio(
//       "負担割",
//       futanWariData,
//       1,
//       FutanWariValidator.validate,
//       mRep[Koukikourei, Int](_.futanWari, i => s"${i}割")
//     ),
//     Prop.date(
//       "期限開始",
//       None,
//       ValidFromValidator.validateOption,
//       mRepDate[Koukikourei](_.validFrom)
//     ),
//     Prop.validUpto(
//       "期限終了",
//       None,
//       ValidUptoValidator.validate,
//       mRepValidUpto[Koukikourei](_.validUpto)
//     )
//   )

//   val ele = Prop.panel(props)

//   def validateForEnter(patientId: Int): Either[String, Koukikourei] =
//     ???
//     // KoukikoureiValidator.validate
//     //   .tupled(
//     //     KoukikoureiIdValidator.validateForEnter *:
//     //       PatientIdValidator.validate(patientId) *: Prop.resultsOf(props)
//     //   )
//     //   .asEither
