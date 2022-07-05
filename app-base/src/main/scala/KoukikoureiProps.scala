package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.Koukikourei
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.KoukikoureiValidator.{*, given}
import dev.myclinic.scala.web.appbase.KoukikoureiValidator
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.DateUtil
import dev.fujiwara.domq.ModelProps

case class KoukikoureiProps(var modelOpt: Option[Koukikourei]) extends ModelProps[Koukikourei] with PropUtil[Koukikourei]:

  val props = (
    TextProp[HokenshaBangouError.type, String](
      "保険者番号",
      _.hokenshaBangou,
      HokenshaBangouValidator.validate
    ),
    TextProp[HihokenshaBangouError.type, String](
      "被保険者番号",
      _.hihokenshaBangou,
      HihokenshaBangouValidator.validate
    ),
    RadioProp[FutanWariError.type, Int](
      "負担割",
      List("１割" -> 1, "２割" -> 2, "３割" -> 3),
      1,
      _.futanWari,
      FutanWariValidator.validate
    ),
    DateProp[ValidFromError.type](
      "期限開始",
      _.validFrom,
      ValidFromValidator.validateOption
    ),
    ValidUptoProp[ValidUptoError.type](
      "期限終了",
      _.validUpto,
      ValidUptoValidator.validate,
      suggest = () => Some(suggestValidUpto())
    )
  )

  val (
    hokenshaBangouProp,
    hihokenshaBangouProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  ) = props

  private def suggestValidUpto(): LocalDate =
    val anchor: LocalDate = validFromProp.currentInputValue.getOrElse(LocalDate.now())
    DateUtil.nextDateOf(7, 31, anchor)

  def updateInput(): this.type =
    super.updateInput(props, modelOpt)
    this
  def updateDisp(): this.type =
    super.updateDisp(props, modelOpt)
    this

  val formProps = props
  val dispProps = props

  def formPanel: HTMLElement =
    super.formPanel(formProps)(cls := "koukikourei-form")

  def dispPanel: HTMLElement =
    super.dispPanel(dispProps)(cls := "koukikourei-disp")

  def validatedForEnter(patientId: Int): Either[String, Koukikourei] =
    val rs = resultsOf(props)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *:
          rs
      )
      .asEither

  def validatedForUpdate: Either[String, Koukikourei] =
    val rs = resultsOf(props)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateOptionForUpdate(modelOpt.map(_.koukikoureiId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *:
          rs
      )
      .asEither
