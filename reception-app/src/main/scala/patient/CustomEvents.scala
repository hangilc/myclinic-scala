package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.CustomEventConnect
import dev.myclinic.scala.model.*

object CustomEvents:
  val addShahokokuhoSubblock: CustomEventConnect[(Int, Shahokokuho)] =
    CustomEventConnect[(Int, Shahokokuho)]("add-shahokokuho-subblock")
  val addKoukikoureiSubblock: CustomEventConnect[(Int, Koukikourei)] =
    CustomEventConnect[(Int, Koukikourei)]("add-koukikourei-subblock")
  val addRoujinSubblock: CustomEventConnect[(Int, Roujin)] =
    CustomEventConnect[(Int, Roujin)]("add-roujin-subblock")
  val addKouhiSubblock: CustomEventConnect[(Int, Kouhi)] =
    CustomEventConnect[(Int, Kouhi)]("add-kouhi-subblock")
