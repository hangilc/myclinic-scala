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
import org.scalajs.dom.window

import scala.language.implicitConversions
import dev.myclinic.scala.web.appbase.ReceiptUtil
import java.time.LocalDate

object Mishuu:
  val itemWrapper = div
  val compList = CompSortDataList[MishuuItem, (Visit, Patient, Meisai)](
    itemWrapper,
    MishuuItem.apply.tupled
  )
  val sumArea = div
  private var bundleFiles: Set[String] = Set.empty
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
        button("会計済に", onclick := (doFinished _)),
        a("閉じる", onclick := (doClose _))
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

  def doClose(): Unit =
    bundleFiles.foreach(b => Api.deletePortalTmpFile(b))
    PracticeBus.clearMishuuList()

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
    val outFile = bundlePdfFileName(compList.list.head.patient.patientId)
    for
      files <- op
      _ <- Api.concatPdfFiles(files, outFile)
    yield
      val url = "/portal-tmp/" + outFile
      window.open(url, "_blank")
      bundleFiles = bundleFiles + outFile

  def receiptPdfFileName(visit: Visit): String =
    val at = visit.visitedAt
    val stamp = String.format("%04d%02d%02d", at.getYear, at.getMonthValue, at.getDayOfMonth)
    s"receipt-${visit.patientId}-${visit.visitId}-${stamp}.pdf"

  def bundlePdfFileName(patientId: Int): String =
    val at = LocalDate.now()
    val stamp = String.format("%04d%02d%02d", at.getYear, at.getMonthValue, at.getDayOfMonth)
    s"receipt-bundle-${patientId}-${stamp}.pdf"

