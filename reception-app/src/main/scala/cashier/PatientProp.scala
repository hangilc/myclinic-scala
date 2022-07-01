package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.model.Patient
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.domq.Prop
import dev.myclinic.scala.web.appbase.PatientValidator
import dev.myclinic.scala.web.appbase.PatientValidator.*

case class PatientProp(init: Option[Patient]):
  val props = (
    Prop[Patient, LastNameError.type, String](
      "姓",
      () => input(cls := "last-name-input"),
      _.fold("")(_.lastName),
      (LastNameValidator.validate _)
    )
  )
