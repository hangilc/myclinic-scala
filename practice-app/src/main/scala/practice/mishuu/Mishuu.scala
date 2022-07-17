package dev.myclinic.scala.web.practiceapp.practice.mishuu

import dev.fujiwara.domq.CompSortDataList
import dev.fujiwara.domq.all.{_, given}
import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.practiceapp.practice.PracticeBus
import dev.myclinic.scala.web.practiceapp.practice.mishuu.MishuuItem
import dev.myclinic.scala.web.practiceapp.practice.mishuu.MishuuItem.given
import dev.myclinic.scala.webclient.{Api, global}
import cats.*
import cats.syntax.all.*

import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.ReceiptUtil

object Mishuu:
  val itemWrapper = div
  val compList = CompSortDataList[MishuuItem, (Visit, Patient, Meisai)](
    itemWrapper,
    MishuuItem.apply.tupled
  )
  val sumArea = div
  val ele = div(
    displayNone,
    cls := "practice-right-widget",
    div(cls := "title", "未収リスト"),
    div(
      cls := "body",
      itemWrapper,
      sumArea,
      div(
        button("領収書PDF", onclick := (() => doReceiptPdf())),
        button("会計済に"),
        a("閉じる")
      )
    )
  )

  PracticeBus.mishuuListChanged.subscribe(list =>
    println(("mishuu", list))
    if list.isEmpty then ele(displayNone)
    else
      compList.sync(list)
      val sum = list.map(_._3.charge).sum
      updateSum(sum)
      ele(displayDefault)
  )

  def updateSum(value: Int): Unit =
    sumArea(innerText := s"合計 ${value}円")

  def doReceiptPdf(): Unit =
    val op = compList.list.map(c =>
      val (visit, patient, meisai) = (c.visit, c.patient, c.meisai)
      val file = receiptPdfFileName((visit))
      for
        visitEx <- Api.getVisitEx(visit.visitId)
        _ <- ReceiptUtil.createReceiptPdf(patient, visitEx, meisai, file)
        _ <- Api.stampPdf(file, "receipt")
      yield file
    ).sequence
    for
      files <- op
    yield ()

  def receiptPdfFileName(visit: Visit): String =
    val at = visit.visitedAt
    val stamp = String.format("receipt-%04d%02d%02d", at.getYear, at.getMonthValue, at.getDayOfMonth)
    s"${visit.patientId}-${visit.visitId}-${stamp}.pdf"
