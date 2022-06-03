package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}

object PatientManip:
  val cashierButton = button

  val ele = div(
    displayNone,
    attr("id") := "practice-patient-manip",
    cashierButton("会計"),
    button("患者終了"),
    a("診察登録"),
    a("文章検索"),
    a("画像保存"),
    a("画像一覧")
  )

  PracticeBus.patientChanged.subscribe {
    case None => ele(displayNone)
    case Some(_) => ele(displayDefault)
  }

  PracticeBus.visitIdChanged.subscribe {
    case None => cashierButton(disabled := true)
    case Some(_) => cashierButton(disabled := false)
  }
