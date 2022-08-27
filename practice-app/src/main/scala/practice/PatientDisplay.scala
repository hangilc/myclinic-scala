package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate
import dev.fujiwara.domq.DispPanel

class PatientDisplay:
  import PatientDisplay as Helper
  val nameSpan = span
  val detailWrapper = div
  val addressPart = div
  val phonePart = div
  val detailDisp = new DispPanel()
  detailDisp.add("住所：", addressPart)
  detailDisp.add("電話：", phonePart)
  val ele = div(
    cls := "practice-patient-display",
    displayNone,
    div(nameSpan, a("詳細", onclick := (onDetail _), cls := "detail-link")),
    detailWrapper(displayNone, cls := "patient-detail", detailDisp.ele)
  )

  val unsubscribe = PracticeBus.patientVisitChanged.subscribe(state =>
    state.patientOption match {
      case Some(patient) =>
        nameSpan(innerText := Helper.formatPatient(patient))
        addressPart(innerText := patient.address)
        phonePart(innerText := patient.phone)
        ele(displayDefault)
      case None => ele(displayNone)
    }
  )

  def dispose: Unit = unsubscribe.unsubscribe()

  private def onDetail(): Unit =
    detailWrapper.toggle()

object PatientDisplay:
  def formatPatient(patient: Patient): String =
    String.format(
      "[%d] %s（%s%s）%d才 %s性",
      patient.patientId,
      patient.fullName(),
      patient.lastNameYomi,
      patient.firstNameYomi,
      DateUtil.calcAge(patient.birthday, LocalDate.now()),
      patient.sex.rep
    )
