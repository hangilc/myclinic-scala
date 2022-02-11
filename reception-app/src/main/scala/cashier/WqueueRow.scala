package dev.myclinic.scala.web.reception.cashier

import dev.myclinic.scala.model.*
import dev.myclinic.scala.web.appbase.{SyncedComp, EventFetcher}
import dev.fujiwara.domq.all.{*, given}
import org.scalajs.dom.HTMLElement
import dev.fujiwara.kanjidate.KanjiDate
import dev.myclinic.scala.util.DateUtil
import java.time.LocalDate

class WqueueRow(_gen: Int, _wq: Wqueue, visit: Visit, patient: Patient)(
  using DataId[Wqueue], ModelSymbol[Wqueue], EventFetcher
)
    extends SyncedComp[Wqueue](_gen, _wq):
  val stateLabelCell: HTMLElement = Table.cell
  val patientIdCell: HTMLElement = Table.cell(patient.patientId.toString)
  val nameCell: HTMLElement = Table.cell(patient.fullName())
  val yomiCell: HTMLElement = Table.cell(patient.fullNameYomi())
  val sexCell: HTMLElement = Table.cell(patient.sex.rep)
  val birthdayCell: HTMLElement = Table.cell(birthday)
  val ageCell: HTMLElement = Table.cell(age.toString)
  val manageCell: HTMLElement = Table.cell
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
  ele(cls := s"wqueue-row-${_wq.visitId}")

  def updateUI(): Unit =
    stateLabelCell(innerText := currentData.waitState.label)
    manageCell(children := List.empty)

  def birthday: String = KanjiDate.dateToKanji(
    patient.birthday,
    formatYear = i => s"${i.gengouAlphaChar}${i.nen}",
    formatMonth = i => s".${i.month}",
    formatDay = i => s".${i.day}",
    formatYoubi = _ => ""
  )
  def age:Int = DateUtil.calcAge(patient.birthday, LocalDate.now())

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
