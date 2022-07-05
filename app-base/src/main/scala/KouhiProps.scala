package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.Kouhi
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.KouhiValidator.{*, given}
import dev.myclinic.scala.web.appbase.KouhiValidator
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.ModelProps

case class KouhiProps(var modelOpt: Option[Kouhi]) extends ModelProps[Kouhi] with PropUtil[Kouhi]:

  val props = (
    TextProp[FutanshaError.type, Int](
      "負担者番号",
      _.futansha,
      FutanshaValidator.validate
    ),
    TextProp[JukyuushaError.type, Int](
      "受給者番号",
      _.jukyuusha,
      JukyuushaValidator.validate
    ),
    DateProp[ValidFromError.type](
      "期限開始",
      _.validFrom,
      ValidFromValidator.validateOption
    ),
    ValidUptoProp[ValidUptoError.type](
      "期限終了",
      _.validUpto,
      ValidUptoValidator.validate
    )
  )

  val (futanshaProp, jukyuushaProp, validFromProp, validUptoProp) = props

    def updateInput(): this.type =
    super.updateInput(props, modelOpt)
    this
  def updateDisp(): this.type =
    super.updateDisp(props, modelOpt)
    this

  val formProps = props
  val dispProps = props

  def formPanel: HTMLElement =
    formPanel(formProps)(cls := "kouhi-form")

  def dispPanel: HTMLElement =
    dispPanel(dispProps)(cls := "kouhi-disp")

  def validatedForEnter(patientId: Int): Either[String, Kouhi] =
    val rs = resultsOf(props)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateForEnter *:
          rs
          ++ Tuple(PatientIdValidator.validate(patientId))
      )
      .asEither

  def validatedForUpdate: Either[String, Kouhi] =
    val rs = resultsOf(props)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateOptionForUpdate(modelOpt.map(_.kouhiId)) *:
          rs 
          ++ Tuple(PatientIdValidator.validateOption(modelOpt.map(_.patientId)))
      )
      .asEither

