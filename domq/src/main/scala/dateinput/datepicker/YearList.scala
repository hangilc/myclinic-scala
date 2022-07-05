package dev.fujiwara.domq.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.Gengou
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate

case class YearList(from: Int, upto: Int):
  val selection = Selection[Int](
    (from to upto).toList,
    year => div(format(year), cls := "domq-word-break-keep-all")
  )
  val ele = selection.ele

  def format(year: Int): String =
    val (era, nen) = Gengou.dateToEra(LocalDate.of(year, 12, 31))
    f"${KanjiDate.eraName(era)}${nen}%02d年 (${year})"
