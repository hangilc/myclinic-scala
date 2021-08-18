package dev.myclinic.scala.model

sealed trait Sex

object Sex {
  object Male extends Sex
  object Female extends Sex  
}