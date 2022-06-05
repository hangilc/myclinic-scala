package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.web.practiceapp.practice.patientmanip.CashierDialog
import dev.myclinic.scala.webclient.{Api, global}

object PatientManip:
  val cashierButton = button

  val ele = div(
    displayNone,
    attr("id") := "practice-patient-manip",
    cashierButton("会計", onclick := (doCashier _)),
    button("患者終了"),
    a("診察登録"),
    a("文章検索"),
    a("画像保存"),
    a("画像一覧")
  )

  PracticeBus.patientVisitChanged.subscribe {
    case NoSelection => ele(displayNone)
    case Browsing(_) =>
      ele(displayDefault)
      cashierButton(enabled := false)
    case Practicing(_, _) =>
      ele(displayDefault)
      cashierButton(enabled := true)
  }

  def doCashier(): Unit =
    PracticeBus.currentVisitId.foreach(visitId =>
      for meisai <- Api.getMeisai(visitId)
      yield
        val dlog = CashierDialog(meisai, visitId, () =>
          
        )
        dlog.open()
    )
