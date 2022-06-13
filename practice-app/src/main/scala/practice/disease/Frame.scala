package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}

case class Frame(patientId: Int):
  val body = div
  val ele = div(
    cls := "practice-disease-frame",
    div("病名", cls := "practice-disease-frame-title"),
    body(cls := "practice-disease-frame-body"),
    div(cls := "practice-disease-frame-menu",
      a("現行", onclick := (current _)),
      a("追加", onclick := (add _)),
      a("転機", onclick := (tenki _)),
      a("編集", onclick := (edit _))
    )
  )

  def current(): Unit =
    for
      list <- Api.listCurrentDiseaseEx(patientId)
    yield
      val c = Current(list)
      body(clear, c.ele)

  def add(): Unit =
    ()

  def tenki(): Unit =
    ()

  def edit(): Unit =
    ()