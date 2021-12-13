package dev.myclinic.scala.apputil

import dev.myclinic.scala.model.{DrugEx}
import dev.myclinic.scala.model.DrugCategory.*
import dev.myclinic.scala.util.ZenkakuUtil.*

object DrugUtil:
  def drugRep(drug: DrugEx): String =
    lazy val amount = toZenkaku(drug.amount.toString)
    lazy val days = toZenkaku(drug.days.toString)
    val master = drug.master
    drug.category match {
      case Naifuku =>
        s"${master.name}${amount}${master.unit} ${drug.usage} ${days}日分" 
      case Tonpuku => 
        s"${master.name}${amount}${master.unit} ${drug.usage} ${days}回分" 
      case Gaiyou => 
        s"${master.name}${amount}${master.unit} ${drug.usage}"
    }

  def toZenkaku(src: String): String =
    convertChars(src, toZenkakuDigits <+> toZenkakuPeriod)