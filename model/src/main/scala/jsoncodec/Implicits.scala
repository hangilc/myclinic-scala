package dev.myclinic.scala.model.jsoncodec

object Implicits extends DateTime with Model with ClinicOperationCodec
  with Event with AppEventCodec with WaitStateCodec