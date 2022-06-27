package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.ShahokokuhoValidator

case class ShahokokuhoForm(init: Option[Shahokokuho]):
  val hokenshaBangouInput = inputText()
  val hihokenshaKigouInput = inputText()
  val hihokenshaBangouInput = inputText()
  val edabanInput = inputText()
  val honninInput = RadioGroup[Int](
    List("本人" -> 1, "家族" -> 0), initValue = init.map(_.honninStore)
  )
  val koureiInput = RadioGroup[Int](
    List("高齢でない" -> 0, "２割" -> 2, "３割" -> 3),
    initValue = init.map(_.koureiStore)
  )
  val validFromInput = DateOptionInput(init.map(_.validFrom))
  val validUptoInput = DateOptionInput(init.flatMap(_.validUpto.value), formatNone = () => "（期限なし）")(
    using new InitNoneConverter:
      def convert: Option[LocalDate] = validFromInput.value.map(_.plusYears(1).minusDays(1))
  )
  val dp = DispPanel(form = true)
  dp.ele(cls := "reception-shahokokuho-form")
  dp.add("保険者番号", hokenshaBangouInput(cls := "hokensha-bangou-input", value := initValue(_.hokenshaBangou.toString)))
  dp.add("記号・番号", div(
    hihokenshaKigouInput(placeholder := "記号", cls := "hihokensha-kigou-input", value := initValue(_.hihokenshaKigou)), "・",
    hihokenshaBangouInput(placeholder := "番号", cls := "hihokensha-bangou-input", value := initValue(_.hihokenshaBangou))
  ))
  dp.add("枝番", edabanInput(cls := "edaban"))
  dp.add("本人・家族", honninInput.ele(cls := "honnin-input"))
  dp.add("高齢", koureiInput.ele(cls := "kourei-input"))
  dp.add("期限開始", validFromInput.ele(cls := "valid-from-input"))
  dp.add("期限終了", validUptoInput.ele(cls := "valid-upto-input"))

  def ele = dp.ele

  def initValue(f: Shahokokuho => String): String =
    init.map(f).getOrElse("")

  def validateForEnter(patientId: Int): Either[String, Shahokokuho] =
    import ShahokokuhoValidator.*
    ShahokokuhoValidator.validateShahokokuhoForEnter(
      validatePatientId(patientId),
      validateHokenshaBangouInput(hokenshaBangouInput.value),
      validateHihokenshaKigou(hihokenshaKigouInput.value),
      validateHihokenshaBangou(hihokenshaBangouInput.value),
      validateHonnin(honninInput.value),
      validateValidFrom(validFromInput.value),
      validateValidUpto(validUptoInput.value),
      validateKourei(koureiInput.value),
      validateEdaban(edabanInput.value)
    ).asEither

  def validateForUpdate(shahokokuhoId: Int, patientId: Int): Either[String, Shahokokuho] =
    import ShahokokuhoValidator.*
    ShahokokuhoValidator.validateShahokokuho(
      validateShahokokuhoIdOptionForUpdate(init.map(_.shahokokuhoId)),
      validatePatientId(patientId),
      validateHokenshaBangouInput(hokenshaBangouInput.value),
      validateHihokenshaKigou(hihokenshaKigouInput.value),
      validateHihokenshaBangou(hihokenshaBangouInput.value),
      validateHonnin(honninInput.value),
      validateValidFrom(validFromInput.value),
      validateValidUpto(validUptoInput.value),
      validateKourei(koureiInput.value),
      validateEdaban(edabanInput.value)
    ).asEither


