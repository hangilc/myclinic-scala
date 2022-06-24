package dev.fujiwara.dateinput.datepicker

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class YearList(from: Int, upto: Int):
  val selection = Selection[Int](
    (from to upto).toList,
    year => div(year.toString)
  )
  val ele = selection.ele
