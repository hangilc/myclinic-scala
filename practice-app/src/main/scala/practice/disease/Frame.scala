package dev.myclinic.scala.web.practiceapp.practice.disease

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.webclient.{Api, global}
import scala.concurrent.Future
import dev.myclinic.scala.model.*

case class Frame(patientId: Int):
  val body = div
  val ele = div(
    cls := "practice-disease-frame",
    div("病名", cls := "practice-disease-frame-title"),
    body(cls := "practice-disease-frame-body"),
    div(
      cls := "practice-disease-frame-menu",
      a("現行", onclick := (current _)),
      a("追加", onclick := (add _)),
      a("転機", onclick := (tenki _)),
      a("編集", onclick := (edit _))
    )
  )

  def current(): Unit =
    for list <- Api.listCurrentDiseaseEx(patientId)
    yield
      val c = Current(list)
      body(clear, c.ele)

  def add(): Unit =
    for visits <- Api.listVisitByPatientReverse(patientId, 0, 10)
    yield
      val dates = visits.map(_.visitedAt.toLocalDate)
      val c = Add(patientId, dates, Frame.examples)
      body(clear, c.ele)

  def tenki(): Unit =
    for list <- Api.listCurrentDiseaseEx(patientId)
    yield
      val c = Tenki(list, _ => tenki())
      body(clear, c.ele)

  def edit(): Unit =
    for list <- Api.listDiseaseEx(patientId)
    yield
      val c = Edit(
        list,
        (disease, master, adjList) => modify(disease, master, adjList.map(_(1)))
      )
      body(clear, c.ele)

  def modify(
      disease: Disease,
      master: ByoumeiMaster,
      shuushokugoMasters: List[ShuushokugoMaster]
  ): Unit =
    val c = Modify(disease, master, shuushokugoMasters, Frame.examples, current _)
    body(clear, c.ele)

object Frame:
  var examples: List[DiseaseExample] = List.empty

  def init(): Future[Unit] =
    for ex <- Api.listDiseaseExample()
    yield examples = ex
