package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate

case class ShahokokuhoForm(init: Option[Shahokokuho]):
  val hokenshaBangouInput = inputText()
  val hihokenshaKigouInput = inputText()
  val hihokenshaBangouInput = inputText()
  val honninInput = RadioGroup[Int](
    List("本人" -> 1, "家族" -> 0)
  )
  val koureiInput = RadioGroup[Int](
    List("高齢でない" -> 0, "２割" -> 2, "３割" -> 3)
  )
  val validFromInput = DateOptionInput()
  val validUptoInput = DateOptionInput(formatNone = () => "（期限なし）")(
    using new InitNoneConverter:
      def convert: Option[LocalDate] = validFromInput.value.map(_.plusYears(1).minusDays(1))
  )
  val eEdaban = inputText()
  val dp = DispPanel(form = true)
  dp.add("保険者番号", hokenshaBangouInput)
  dp.add("記号・番号", div(
    hihokenshKigouInput(placeholder := "記号")、
    hihokenshaBangouInput(placeholder := "番号")
  ))
  