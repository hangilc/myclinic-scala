package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.myclinicutil.DiseaseUtil

case class Add(patientId: Int):
  val nameSpan = span
  val ele = div(
    cls := "practice-disease-add",
    div("名称：", nameSpan),
    div(input(attr("type") := "date"))
  )