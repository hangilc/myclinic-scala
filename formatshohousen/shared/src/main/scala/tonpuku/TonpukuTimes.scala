package dev.myclinic.scala.formatshohousen.tonpuku

import dev.myclinic.scala.formatshohousen.Formatter
import dev.myclinic.scala.formatshohousen.FormatContext

class TonpukuTimes extends Formatter:
  def format(index: Int, ctx: FormatContext): String =
    ???

object TonpukuTimes:
  def tryParse(lead: String, more: List[String]): Option[TonpukuTimes] =
    ???

