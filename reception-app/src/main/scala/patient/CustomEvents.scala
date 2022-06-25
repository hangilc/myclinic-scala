package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.CustomEventConnect
import dev.myclinic.scala.model.*

object CustomEvents:
  def addHokenSubblock[T](using
      modelSymbol: ModelSymbol[T]
  ): CustomEventConnect[(Int, T)] =
    val key = s"add-${modelSymbol.getSymbol}-subblock"
    CustomEventConnect(key)
