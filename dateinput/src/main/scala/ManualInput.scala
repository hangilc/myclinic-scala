// package dev.fujiwara.dateinput

// import dev.fujiwara.domq.all.{*, given}
// import dev.fujiwara.domq.{Html, DataInputDialog}
// import dev.fujiwara.kanjidate.DateParser

// import java.time.LocalDate
// import dev.fujiwara.kanjidate.KanjiDate

// object ManualInput:
//   def getDateByDialog(
//       onEnter: LocalDate => Unit,
//       init: Option[LocalDate] = None,
//       title: String = "日付の入力",
//       check: LocalDate => Either[String, LocalDate] = Right(_)
//   ): Unit =
//     DataInputDialog[LocalDate](title, formatInitValue(init), convertToDate _, onEnter).open()
//     def convertToDate(src: String): Either[String, LocalDate] =
//       for
//         parsed <-
//           if src.isEmpty then Left("日付が入力されていません。")
//           else DateParser.parse(src).toRight("入力が不適切です。")
//         checked <- check(parsed)
//       yield checked


//   def getDateOptionByDialog(
//       onEnter: Option[LocalDate] => Unit,
//       init: Option[LocalDate] = None,
//       title: String = "日付の入力",
//       check: Option[LocalDate] => Either[String, Option[LocalDate]] = Right(_)
//   ): Unit =
//     DataInputDialog[Option[LocalDate]](
//       title,
//       formatInitValue(init),
//       convertToDate _,
//       onEnter
//     ).open()

//     def convertToDate(src: String): Either[String, Option[LocalDate]] =
//       for
//         parsed <-
//           if src.isEmpty then Right(None)
//           else DateParser.parse(src).toRight("入力が不適切です。").map(Some(_))
//         checked <- check(parsed)
//       yield checked

//   private def formatInitValue(init: Option[LocalDate]): String =
//     init match {
//       case None => ""
//       case Some(d) =>
//         KanjiDate.dateToKanji(
//           d,
//           formatYear = (info => s"${info.gengouAlphaChar}${info.nen}."),
//           formatMonth = (info => s"${info.month}."),
//           formatDay = (info => s"${info.day}")
//         )
//     }
