package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.CustomEventConnect
import dev.myclinic.scala.model.*

object CustomEvents:
  val addShahokokuhoSubblock: CustomEventConnect[(Int, Shahokokuho)] =
    CustomEventConnect[(Int, Shahokokuho)]("add-shahokokuho-subblock")
