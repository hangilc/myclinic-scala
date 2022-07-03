package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.{*, given}
import dev.myclinic.scala.model.Kouhi
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.KouhiValidator.{*, given}
import dev.myclinic.scala.web.appbase.KouhiValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class KouhiProps(var modelOpt: Option[Kouhi]):
  type K = Kouhi

  val props = (
    TextProp[K, FutanshaError.type, Int](
      "負担者番号",
      _.futansha,
      FutanshaValidator.validate
    ),
    TextProp[K, JukyuushaError.type, Int](
      "受給者番号",
      _.jukyuusha,
      JukyuushaValidator.validate
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

  val (futanshaProp, jukyuushaProp, validFromProp, validUptoProp) = props

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

  def validatedForEnter(patientId: Int): Either[String, Kouhi] =
    val rs = Prop.resultsOf(props)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateForEnter *:
          rs
          ++ Tuple(PatientIdValidator.validate(patientId))
      )
      .asEither

  def validatedForUpdate: Either[String, Kouhi] =
    val rs = Prop.resultsOf(props)
    KouhiValidator
      .validate(
        KouhiIdValidator.validateOptionForUpdate(modelOpt.map(_.kouhiId)) *:
          rs 
          ++ Tuple(PatientIdValidator.validateOption(modelOpt.map(_.patientId)))
      )
      .asEither

