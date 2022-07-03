package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.{*, given}
import dev.myclinic.scala.model.Shahokokuho
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.ShahokokuhoValidator.{*, given}
import dev.myclinic.scala.web.appbase.ShahokokuhoValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class ShahokokuhoProps(var modelOpt: Option[Shahokokuho]):
  val props = (
    TextProp[Shahokokuho, HokenshaBangouError.type, Int](
      "保険者番号",
      _.hokenshaBangou,
      HokenshaBangouValidator.validateInput
    ).inputElementClass("hokensha-bangou-input"),
    TextProp[Shahokokuho, HihokenshaKigouError.type, String](
      "被保険者記号",
      _.hihokenshaKigou,
      HihokenshaKigouValidator.validate
    ).inputElementClass("hihokensha-kigou-input"),
    TextProp[Shahokokuho, HihokenshaBangouError.type, String](
      "被保険者番号",
      _.hihokenshaBangou,
      HihokenshaBangouValidator.validate
    ).inputElementClass("hihokensha-bangou-input"),
    RadioProp[Shahokokuho, HonninError.type, Int](
      "本人・家族",
      List("本人" -> 1, "家族" -> 0),
      0,
      _.honninStore,
      HonninValidator.validate
    ),
    DateProp[Shahokokuho, ValidFromError.type](
      "期限開始",
      _.validFrom,
      ValidFromValidator.validateOption
    ),
    ValidUptoProp[Shahokokuho, ValidUptoError.type](
      "期限終了",
      _.validUpto,
      ValidUptoValidator.validate
    ),
    RadioProp[Shahokokuho, KoureiError.type, Int](
      "高齢",
      List("高齢でない" -> 0, "１割" -> 1, "２割" -> 2, "３割" -> 3),
      0,
      _.koureiStore,
      KoureiValidator.validate
    ),
    TextProp[Shahokokuho, EdabanError.type, String](
      "枝番",
      _.edaban,
      EdabanValidator.validate
    )
  )

  val (
    hokenshaBangouProp,
    hihokenshaKigouProp,
    hihokenshaBangouProp,
    honninProp,
    validFromProp,
    validUptoProp,
    koureiProp,
    edabanProp
  ) = props

  val formProps = (
    hokenshaBangouProp,
    (
      "記号・番号",
      div(
        displayBlock,
        hihokenshaKigouProp.inputElement,
        "・",
        hihokenshaBangouProp.inputElement
      )
    ),
    edabanProp,
    koureiProp,
    honninProp,
    validFromProp,
    validUptoProp
  )

  val dispProps = (
    hokenshaBangouProp,
    (
      "記号・番号",
      div(
        displayBlock,
        hihokenshaKigouProp.dispElement,
        "・",
        hihokenshaBangouProp.dispElement
      )
    ),
    edabanProp,
    koureiProp,
    honninProp,
    validFromProp,
    validUptoProp
  )

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

  def formPanel: HTMLElement =
    Prop.formPanel(formProps)

  def dispPanel: HTMLElement =
    Prop.dispPanel(dispProps)

  def validatedForEnter(patientId: Int): Either[String, Shahokokuho] =
    val rs = Prop.resultsOf(props)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate: Either[String, Shahokokuho] =
    val rs = Prop.resultsOf(props)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateForEnter *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither
