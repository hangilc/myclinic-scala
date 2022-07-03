package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.{*, given}
import dev.myclinic.scala.model.Roujin
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.RoujinValidator.{*, given}
import dev.myclinic.scala.web.appbase.RoujinValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class RoujinProps(var modelOpt: Option[Roujin]):
  type K = Roujin

  val props = (
    TextProp[K, ShichousonError.type, Int](
      "市町村番号",
      _.shichouson,
      ShichousonValidator.validateInput
    ),
    TextProp[K, JukyuushaError.type, Int](
      "受給者番号",
      _.jukyuusha,
      JukyuushaValidator.validateInput
    ),
    RadioProp[K, FutanWariError.type, Int](
      "負担割",
      List("１割" -> 1, "２割" -> 2, "３割" -> 3),
      1,
      _.futanWari,
      FutanWariValidator.validate
    ),
    DateProp[K, ValidFromError.type](
      "期限開始",
      _.validFrom,
      ValidFromValidator.validateOption
    ),
    ValidUptoProp[K, ValidUptoError.type](
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
    val updater = new InputUpdater(modelOpt)
    import updater.given
    updater.update(props)
    this
  def updateDisp(): this.type =
    val updater = new DispUpdater(modelOpt)
    import updater.given
    updater.update(props)
    this

  val formProps = props
  val dispProps = props

  def formPanel: HTMLElement =
    Prop.formPanel(formProps)

  def dispPanel: HTMLElement =
    Prop.dispPanel(dispProps)

  def validatedForEnter(patientId: Int): Either[String, Roujin] =
    val rs = Prop.resultsOf(props)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *:
          rs
      )
      .asEither

  def validatedForUpdate: Either[String, Roujin] =
    val rs = Prop.resultsOf(props)
    RoujinValidator
      .validate(
        RoujinIdValidator.validateOptionForUpdate(modelOpt.map(_.roujinId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *:
          rs
      )
      .asEither
