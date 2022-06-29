package dev.myclinic.scala.web.reception.cashier

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.myclinic.scala.model.*
import dev.fujiwara.dateinput.DateOptionInput
import dev.fujiwara.domq.DispPanel
import dev.fujiwara.dateinput.InitNoneConverter
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.validator.KouhiValidator
import dev.myclinic.scala.web.appbase.validator.KouhiValidator.*
import org.scalajs.dom.HTMLElement
import org.scalajs.dom.HTMLInputElement
import dev.myclinic.scala.web.appbase.formprop.*
import dev.myclinic.scala.web.appbase.formprop.Prop.given
import dev.fujiwara.validator.section.Implicits.*

class KouhiForm(init: Option[Kouhi]):
  val props = (
    Prop("負担者番号", input, FutanshaValidator.validate),
    Prop("受給者番号", input, JukyuushaValidator.validate),
    Prop.date("期限開始", None, ValidFromValidator.validateOption),
    Prop.validUpto("期限終了", None, ValidUptoValidator.validate)
  )

  val ele = Prop.panel(props)

  def validaForEnter(patientId: Int): Either[String, Kouhi] =
    val rs = (KouhiIdValidator.validateForEnter *: Prop.resultsOf(props))
      ++ Tuple(PatientIdValidator.validate(patientId))
    KouhiValidator.validate(rs).asEither



