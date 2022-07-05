package dev.myclinic.scala.web.appbase

import dev.myclinic.scala.model.Roujin
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.RoujinValidator.{*, given}
import dev.myclinic.scala.web.appbase.RoujinValidator
import dev.fujiwara.kanjidate.KanjiDate
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.ModelProps

case class RoujinProps(var modelOpt: Option[Roujin]) extends ModelProps[Roujin] with PropUtil[Roujin]:

  val props = (
    TextProp[ShichousonError.type, Int](
      "市町村番号",
      _.shichouson,
      ShichousonValidator.validateInput
    ),
    TextProp[JukyuushaError.type, Int](
      "受給者番号",
      _.jukyuusha,
      JukyuushaValidator.validateInput
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
      ValidUptoValidator.validate
    )
  )

  val (
    shichousonProp,
    jukyuushaProp,
    futanWariProp,
    validFromProp,
    validUptoProp
  ) = props

  def updateInput(): this.type =
    super.updateInput(props, modelOpt)
    this
  def updateDisp(): this.type =
    super.updateDisp(props, modelOpt)
    this

  val formProps = props
  val dispProps = props

  def formPanel: HTMLElement =
    formPanel(formProps)(cls := "roujin-form")

  def dispPanel: HTMLElement =
    dispPanel(dispProps)(cls := "roujin-disp")

  def validatedForEnter(patientId: Int): Either[String, Roujin] =
    val rs = resultsOf(props)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *:
          rs
      )
      .asEither

  def validatedForUpdate: Either[String, Roujin] =
    val rs = resultsOf(props)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateOptionForUpdate(modelOpt.map(_.roujinId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *:
          rs
      )
      .asEither

      
