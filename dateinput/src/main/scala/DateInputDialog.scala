package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import dev.fujiwara.domq.DataInputDialog
import dev.fujiwara.kanjidate.DateParser

class DateInputDialog(
    init: LocalDate,
    format: LocalDate => String = DateInputDialog.defaultFormat,
    parse: String => Either[String, LocalDate] = DateInputDialog.defaultParse,
    title: String = "日付の入力"
) extends DataInputDialog[LocalDate](title, format(init), parse)

object DateInputDialog:
  val defaultFormat: LocalDate => String = d =>
    val info = KanjiDate.DateInfo(d)
    s"${info.gengouAlphaChar}${info.nen}.${info.month}.${info.day}"

  val defaultParse: String => Either[String, LocalDate] = src =>
    DateParser.parse(src).toRight("日付の入力が不適切です。")

class DateOptionInputDialog(
    init: Option[LocalDate],
    format: LocalDate => String = DateOptionInputDialog.defaultFormat,
    formatNone: () => String = DateOptionInputDialog.defaultFormatNone,
    parse: String => Either[String, Option[LocalDate]] = DateOptionInputDialog.defaultParse,
    title: String = "日付の入力"
) extends DataInputDialog[Option[LocalDate]](
      title,
      init.fold(formatNone())(format),
      parse
    )

object DateOptionInputDialog:
  val defaultFormat: LocalDate => String = DateInputDialog.defaultFormat

  val defaultFormatNone: () => String = () => ""

  val defaultParse: String => Either[String, Option[LocalDate]] = src =>
    if src.isEmpty then Right(None)
    else DateInputDialog.defaultParse(src).map(Some(_))
