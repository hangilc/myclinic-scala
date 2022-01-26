package dev.myclinic.scala.web.appbase

object HotlineEnv:
  private val hotlineNameRepMap: Map[String, String] =
    Map("practice" -> "診療", "reception" -> "受付")

  def hotlineNameRep(code: String): String =
    hotlineNameRepMap.getOrElse(code, code)
