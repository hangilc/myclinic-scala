package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.ModelProp
import dev.fujiwara.domq.ModelInput
import dev.fujiwara.domq.ModelInputs
import dev.fujiwara.domq.ModelInputProcs
import dev.myclinic.scala.model.*
import PatientValidator.*
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.DispPanel

object ShahokokuhoProps:
  class HokenshaBangouProp extends ModelProp("保険者番号")
  class HihokenshaKigouProp extends ModelProp("被保険者記号")
  class HihokenshaBangouProp extends ModelProp("被保険者番号")
  class HonninProp extends ModelProp("本人・家族")
  class ValidFromProp extends ModelProp("期限開始")
  class ValidUptoProp extends ModelProp("期限終了")
  class KoureiProp extends ModelProp("高齢")
  class EdabanProp extends ModelProp("枝番")

  object hokenshaBangouProp extends HokenshaBangouProp
  object hihokenshaKigouProp extends HihokenshaKigouProp
  object hihokenshaBangouProp extends HihokenshaBangouProp
  object honninProp extends HonninProp
  object validFromProp extends ValidFromProp
  object validUptoProp extends ValidUptoProp
  object koureiProp extends KoureiProp
  object edabanProp extends EdabanProp

  val props = (
    hokenshaBangouProp,
    hihokenshaKigouProp,
    hihokenshaBangouProp,
    honninProp,
    validFromProp,
    validUptoProp,
    koureiProp,
    edabanProp
  )

case class ShahokokuhoInputs(modelOpt: Option[Shahokokuho])
    extends ModelInput[Shahokokuho]
    with ModelInputs[Shahokokuho]
    with ModelInputProcs[Shahokokuho]:
  class HokenshaBangouInput
      extends TextInput[HokenshaBangouError.type, Int](
        "保険者番号",
        _.hokenshaBangou,
        HokenshaBangouValidator.validateInput
      )

  class HihokenshaKigouInput
      extends TextInput[HokenshaBangouError.type, Int](
        "被保険者記号",
        _.hihokenshaKigou,
        HihokenshaKigouValidator.validate
      )

  class HihokenshaBangouInput
      extends TextInput[HokenshaBangouError.type, Int](
        "被保険者番号",
        _.hihokenshaBangou,
        HihokenshaBangouValidator.validate
      )

  class HonninInput
      extends RadioInput[HonninError.type, Int](
        "本人・家族",
        _.honninStore,
        HonninValidator.validate,
        List("本人" -> 1, "家族" -> 0),
        0
      )

  class ValidFromInput
      extends DateInput[ValidFromError.type, LocalDate](
        "期限終了",
        _.validFrom,
        ValidFromValidator.validate
      )

  class ValidUptoInput
      extends ValidUptoInput[ValidUptoError.type, ValidUpto](
        "期限終了",
        _.validUpto,
        ValidUptoValidator.validate
      )

  class KoureiInput
      extends RadioInput[KoureiError.type, Int](
        "高齢",
        _.koureiStore,
        KoureiValidator.validate,
        ShahokokuhoValidator.validKoureiValues,
        0
      )

  class EdabanInput
      extends TextInput[HokenshaBangouError.type, Int](
        "枝番",
        _.edaban,
        EdabanValidator.validate
      )

  type Create[P] = P match {
    case ShahokokuhoProps.HokenshaBangouProp => HokenshaBangouInput
    case ShahokokuhoProps.HihokenshaKigouProp => HihokenshaKigouInput
    case ShahokokuhoProps.HihokenshaBangouProp => HihokenshaBangouInput
    case ShahokokuhoProps.HonninProp => HonninInput
    case ShahokokuhoProps.ValidFromProp => ValidFromInput
    case ShahokokuhoProps.ValidUptoProp => ValidUptoInput
    case ShahokokuhoProps.KoureiProp => KoureiInput
    case ShahokokuhoProps.EdabanProp => EdabanInput  
  }

  def fCreate[P](p: P) = p match {
    case _: ShahokokuhoProps.HokenshaBangouProp => new HokenshaBangouInput()
    case _: ShahokokuhoProps.HihokenshaKigouProp => new HihokenshaKigouInput()
    case _: ShahokokuhoProps.HihokenshaBangouProp => new HihokenshaBangouInput()
    case _: ShahokokuhoProps.HonninProp => new HonninInput()
    case _: ShahokokuhoProps.ValidFromProp => new ValidFromInput()
    case _: ShahokokuhoProps.ValidUptoProp => new ValidUptoInput()
    case _: ShahokokuhoProps.KoureiProp => new KoureiInput()
    case _: ShahokokuhoProps.EdabanProp => new EdabanInput  ()
  }

  def create(props: Tuple): Tuple.Map[props.type, Create] =
    props.map([T] => (t: T) => fCreate(t))

  val inputs = create(ShahokokuhoProps.props)

  def update(): Unit =
    update(inputs, modelOpt)

  def inputForm: HTMLElement =
    createForm(ShahokokuhoProps.props, inputs)

  def validatedForEnter(patientId: Int): Either[String, Shahokokuho] =
    val rs = resultsOf(props)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateForEnter *:
          PatientIdValidator.validate(patientId) *: rs
      )
      .asEither

  def validatedForUpdate(): Either[String, Shahokokuho] =
    val rs = resultsOf(props)
    ShahokokuhoValidator
      .validate(
        ShahokokuhoIdValidator.validateOptionForUpdate(modelOpt.map(_.shahokokuhoId)) *:
          PatientIdValidator.validateOption(modelOpt.map(_.patientId)) *: rs
      )
      .asEither












