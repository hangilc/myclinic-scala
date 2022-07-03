package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.{*, given}
import dev.myclinic.scala.model.Koukikourei
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.KoukikoureiValidator.{*, given}
import dev.myclinic.scala.web.appbase.KoukikoureiValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class KoukikoureiProps(var modelOpt: Option[Koukikourei]):
  type K = Koukikourei

  val props = (
    TextProp[K, HokenshaBangouError.type, String](
      "保険者番号",
      _.hokenshaBangou,
      HokenshaBangouValidator.validate
    ),
    TextProp[K, HihokenshaBangouError.type, String](
      "被保険者番号",
      _.hihokenshaBangou,
      HihokenshaBangouValidator.validate
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
    hokenshaBangouProp,
    hihokenshaBangouProp,
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

  def validatedForEnter(patientId: Int): Either[String, Koukikourei] =
    val rs = Prop.resultsOf(props)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *:
          rs
      )
      .asEither

  def validatedForUpdate: Either[String, Koukikourei] =
    val rs = Prop.resultsOf(props)
    KoukikoureiValidator
      .validate(
        KoukikoureiIdValidator.validateOptionForUpdate(modelOpt.map(_.koukikoureiId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *:
          rs
      )
      .asEither
