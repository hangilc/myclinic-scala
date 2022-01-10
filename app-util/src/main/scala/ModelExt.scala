package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.*
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import java.time.LocalDate

object ModelExt:
  extension (p: Patient)
    def birthdayRep: String =
      KanjiDate.dateToKanji(p.birthday)
    def age: Int =
      DateUtil.calcAge(p.birthday, LocalDate.now())

