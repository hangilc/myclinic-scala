package dev.myclinic.scala.apputil

import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.model.ValidUpto

object EffectivePeriod:
  def repValidFrom(validFrom: LocalDate): String = KanjiDate.dateToKanji(validFrom)
  def repValidUpto(validUpto: ValidUpto): String = repValidUpto(validUpto.value)
  def repValidUpto(validUpto: Option[LocalDate]): String =
    validUpto match {
      case Some(d) => KanjiDate.dateToKanji(d)
      case None => "（期限なし）"
    }
