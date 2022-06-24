package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.EventFetcher
import dev.fujiwara.domq.all.{*, given}
import dev.fujiwara.domq.DataSource
import org.scalajs.dom.HTMLElement
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate

class WqueueRow(ds: DataSource[(Wqueue, Visit, Patient)])(
  using DataId[Wqueue], ModelSymbol[Wqueue], EventFetcher
):
  val stateLabelCell: HTMLElement = Table.cell
  val patientIdCell: HTMLElement = Table.cell //Table.cell(patient.patientId.toString)
  val nameCell: HTMLElement = Table.cell //Table.cell(patient.fullName())
  val yomiCell: HTMLElement = Table.cell //Table.cell(patient.fullNameYomi())
  val sexCell: HTMLElement = Table.cell //Table.cell(patient.sex.rep)
  val birthdayCell: HTMLElement = Table.cell //Table.cell(birthday)
  val ageCell: HTMLElement = Table.cell //Table.cell(age.toString)
  val manageCell: HTMLElement = Table.cell //Table.cell
  val ele = Table.createRow(List(
    stateLabelCell,
    patientIdCell,
    nameCell,
    yomiCell,
    sexCell,
    birthdayCell,
    ageCell,
    manageCell
  ))
  ele(cls := s"wqueue-row-${wqData.visitId}")

  def wqData: Wqueue = ds.data._1
  def visitData: Visit = ds.data._2
  def patientData: Patient = ds.data._3

  def updateUI(): Unit =
    stateLabelCell(innerText := wqData.waitState.label)
    manageCell(children := List.empty)
    patientIdCell(innerText := patientData.patientId.toString)
    nameCell(innerText := patientData.fullName())
    yomiCell(innerText := patientData.fullNameYomi())
    sexCell(innerText := patientData.sex.rep)
    birthdayCell(innerText := birthday(patientData.birthday))
    ageCell(innerText := age(patientData.birthday).toString)
    manageCell()
  
  def birthday(d: LocalDate): String = KanjiDate.dateToKanji(
    d,
    formatYear = i => s"${i.gengouAlphaChar}${i.nen}",
    formatMonth = i => s".${i.month}",
    formatDay = i => s".${i.day}",
    formatYoubi = _ => ""
  )

  def age(birthday: LocalDate):Int = DateUtil.calcAge(birthday, LocalDate.now())


        // e => {
        //   if wq.waitState == WaitState.WaitCashier then
        //     e(
        //       button("会計")(
        //         onclick := (() =>
        //           doCashier(wq.visitId, patient, visit.visitedAt.toLocalDate)
        //         )
        //       )
        //     )
        //   if wq.waitState == WaitState.WaitExam then
        //     e(a("削除", onclick := (() => doDelete(visit, patient))))
        // }
