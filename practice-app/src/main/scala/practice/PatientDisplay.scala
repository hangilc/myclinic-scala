package dev.myclinic.scala.web.practiceapp.practice

import dev.fujiwara.domq.all.{*, given}
import dev.myclinic.scala.model.Patient
import scala.language.implicitConversions
import dev.myclinic.scala.web.practiceapp.PracticeBus
import dev.fujiwara.kanjidate.DateUtil
import java.time.LocalDate
import dev.fujiwara.domq.DispPanel
import scala.util.matching.Regex
import dev.myclinic.scala.util.StringUtil
import org.scalajs.dom.HTMLElement
import dev.myclinic.scala.web.practiceapp.practice.twilio.Call
import dev.myclinic.scala.webclient.global
import dev.myclinic.scala.web.practiceapp.practice.twilio.TwilioPhone

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
        detailWrapper(displayNone)
        nameSpan(innerText := Helper.formatPatient(patient))
        addressPart(innerText := patient.address)
        phonePart(clear, Helper.parsePhone(patient.phone))
        ele(displayDefault)
      case None =>
        detailWrapper(displayNone)
        nameSpan(clear)
        addressPart(clear)
        phonePart(clear)
        ele(displayNone)
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

  case class PhoneNumber(s: String)
  case class PhoneText(s: String)

  val phonePattern: Regex = raw"\+?[0-9-]+".r

  def parsePhone(phone: String): List[HTMLElement] =
    StringUtil
      .classify(
        phonePattern,
        phone,
        PhoneNumber.apply,
        PhoneText.apply
      )
      .flatMap {
        case PhoneText(s) =>
          s.trim match {
            case "" => List.empty
            case s  => List(span(s))
          }
        case PhoneNumber(s) =>
          TwilioPhone.canonicalPhoneNumber(s) match {
            case Some(phone) =>
              List(
                span(s),
                button(
                  "発信",
                  onclick := (() => {
                    if !PracticeBus.twilioPhone.call(phone) then
                      ShowMessage.showError("電話を使用できません。")
                  })
                ),
                button(
                  "終了",
                  onclick := (() => PracticeBus.twilioPhone.hangup())
                )
              )
            case None => List(span(s))
          }
      }
