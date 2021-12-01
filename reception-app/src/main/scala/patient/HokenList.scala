package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.{KanjiDate, DateUtil, HokenRep}
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.webclient.Api
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}

class HokenList(patientId: Int):
  val errorBox = ErrorBox()
  val eDisp = div()
  val eCommands = div()
  val ele = div(
    errorBox.ele,
    eDisp,
    eCommands(
      checkbox(),
      span("過去の保険も含める  ")
    )
  )

  def init(): Unit =
    val date = LocalDate.now()
    val f = 
      for
        patient <- Api.getPatient(patientId)
        shahoOpt <- Api.findAvailableShahokokuho(patient.patientId, date)
        roujinOpt <- Api.findAvailableRoujin(patient.patientId, date)
        koukikoureiOpt <- Api.findAvailableKoukikourei(patient.patientId, date)
        kouhiList <- Api.listAvailableKouhi(patient.patientId, date)
      yield {
        val list = shahoOpt.map(ShahokokuhoHokenItem(_)).toList
          ++ roujinOpt.map(RoujinHokenItem(_)).toList
          ++ koukikoureiOpt.map(KoukikoureiHokenItem(_)).toList
          ++ kouhiList.map(KouhiHokenItem(_))
        eDisp.innerHTML = ""
        eDisp(
          (list.map(_.rep): List[Modifier]): _*
        )
      }
    f.onComplete {
      case Success(_) => ()
      case Failure(ex) => errorBox.show(ex.getMessage)
    }


sealed trait HokenItem:
  def rep: String

class ShahokokuhoHokenItem(shahokokuho: Shahokokuho) extends HokenItem:
  def rep: String = HokenRep.shahokokuhoRep(
    shahokokuho.hokenshaBangou,
    shahokokuho.koureiFutanWari
  )

class RoujinHokenItem(roujin: Roujin) extends HokenItem:
  def rep: String = HokenRep.roujinRep(roujin.futanWari)

class KoukikoureiHokenItem(koukikourei: Koukikourei) extends HokenItem:
  def rep: String = HokenRep.koukikoureiRep(koukikourei.futanWari)

class KouhiHokenItem(kouhi: Kouhi) extends HokenItem:
  def rep: String = HokenRep.kouhiRep(kouhi.futansha)
