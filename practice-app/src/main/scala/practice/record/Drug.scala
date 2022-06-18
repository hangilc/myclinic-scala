package dev.myclinic.scala.web.practiceapp.practice.record

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.apputil.HokenUtil
import dev.myclinic.scala.model.VisitEx
import dev.myclinic.scala.apputil.DrugUtil
import dev.myclinic.scala.util.ZenkakuUtil
import scala.language.implicitConversions

class Drug(visit: VisitEx):
  val ele = div()

  if visit.drugs.size > 0 then
    ele(div("Ｒｐ）"))
    visit.drugs.zipWithIndex.foreach {
      case (drug, index) => 
        val i =  ZenkakuUtil.convertToZenkakuDigits((index+1).toString)
        val s = DrugUtil.drugRep(drug)
        ele(div(s"$i）$s"))
    }
