package dev.myclinic.scala.web.reception

case class Hotline(message: String, sender: String, recipient: String)

object Hotline:
  def post(hotline: Hotline): Unit =
    ???