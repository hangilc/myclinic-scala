package dev.myclinic.scala.web.reception

object Setting:
  def hotlineSender: String = "reception"
  def hotlineRecipient: String = "practice"

  private val hotlineNameRepMap: Map[String, String] =
    Map("practice" -> "診療", "reception" -> "受付")

  def hotlineNameRep(code: String): String =
    hotlineNameRepMap.getOrElse(code, "不明")
