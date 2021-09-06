package dev.myclinic.scala.model

trait ModelJsonCodec {
  def encodeAppoint(value: Appoint): String
  def decodeAppoint(src: String): Appoint

}

