package dev.myclinic.scala.web.appbase

object HotlineEnv:
  private val hotlineNameRepMap: Map[String, String] =
    Map("practice" -> "診療", "reception" -> "受付")

  def hotlineNameRep(code: String): String =
    hotlineNameRepMap.getOrElse(code, code)

  private val regularsMap: Map[String, List[String]] =
    Map(
      "reception" -> List(
        "おはようございます。",
        "退出します。",
        "戻りました。",
        "検温中です。",
        "体温 {} 度でした。",
        "胃腸の調子が悪いそうです。",
        "相談です。",
        "セットできました。",
        "面会の肩がいらしてます。"
      ),
      "practice" -> List(
        "おはようございます。",
        "診察室におねがいします。",
        "処方箋おねがいします。",
        "検査伝票おねがいします。"
      )
    )

  def regulars(code: String): List[String] =
    regularsMap.getOrElse(code, List.empty)