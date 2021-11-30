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

class HokenList(var list: List[HokenItem]):
  val ele = div(
    (list.map(item => div(item.rep)): List[Modifier]): _*
  )

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
