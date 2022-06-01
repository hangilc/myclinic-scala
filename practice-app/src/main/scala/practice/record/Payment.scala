package dev.myclinic.scala.web.practiceapp.practice.record

import dev.myclinic.scala.web.practiceapp.practice.record.payment.Disp
import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.{Payment as ModelPayment, *}

class Payment(chargeOption: Option[Charge], paymentOption: Option[ModelPayment], visitId: Int):
  val ele = div(
    Disp(chargeOption, paymentOption, visitId).ele
  )
