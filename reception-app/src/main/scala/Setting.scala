package dev.myclinic.scala.web.reception

import dev.myclinic.scala.web.appbase.HotlineEnv

object Setting:
  def hotlineSender: String = "reception"
  def hotlineRecipient: String = "practice"

  // private val hotlineNameRepMap: Map[String, String] =
  //   Map("practice" -> "診療", "reception" -> "受付")

  // def hotlineNameRep(code: String): String =
  //   hotlineNameRepMap.getOrElse(code, "不明")

  def hotlineNameRep(code: String): String = HotlineEnv.hotlineNameRep(code)

  val regularHotlineMessages: List[String] =
    List(
      "おはようございます。体温 {} 度でした。",
      "退出します。",
      "戻りました。",
      "検温中です。",
      "体温 {} 度でした。",
      "胃腸の調子が悪いそうです。",
      "相談です。",
      "セットできました。",
      "面会の肩がいらしてます。",
    )
