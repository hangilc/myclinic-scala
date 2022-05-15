package dev.myclinic.scala.formatshohousen

import RegexPattern.*
import dev.myclinic.scala.formatshohousen.tonpuku.TonpukuTotal

object MiscFormatter:
  def tryParse(lead: String, more: List[String]): Option[Formatter] =
    tryParseRelenza(lead, more)

  def tryParseRelenza(lead: String, more: List[String]): Option[Formatter] =
    val pattern = s"(リレンザ)$space+($digit+ブリスター)$space+(１回$digit+ブリスター)$space+(１日$digit+回)$space*".r
    (lead :: more).mkString(zenkakuSpace) match {
      case pattern(name, amount, usage1, usage2) => 
        Some(new TonpukuTotal(name, amount, s"$usage1、$usage2"))
      case _ => None
    }