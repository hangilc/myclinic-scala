package dev.myclinic.scala.db

import dev.myclinic.scala.model._

trait JsonEncoder {
  def toJson(value: Appoint): String
}