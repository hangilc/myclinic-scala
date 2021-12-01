package dev.myclinic.scala.web.reception.patient

import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.domq.{Icons, Form, ErrorBox, Modifier}
import scala.language.implicitConversions
import org.scalajs.dom.raw.{HTMLElement, HTMLInputElement}
import dev.myclinic.scala.util.{KanjiDate, DateUtil}
import dev.myclinic.scala.model.*
import java.time.LocalDate
import dev.myclinic.scala.web.appbase.DateInput

class ShahokokuhoForm(shahokokuho: Shahokokuho):
  val eHokenshaBangou = inputText()
  val eHihokenshaKigou = inputText()
  val eHihokenshaBangou = inputText()
  val eHonninForm = form()
  val eValidFrom = DateInput()
  val eValidUpto = DateInput()
  val ele = Form.rows(
    span("保険者番号") -> eHokenshaBangou(
      cls := "hokensha-bangou-input",
      attr("value") := shahokokuho.hokenshaBangou.toString
    ),
    span("被保険者") -> div(
      eHihokenshaKigou(cls := "hihokensha-kigou"),
      eHihokenshaBangou(cls := "hihokensha-bangou")
    ),
    span("本人・家族") -> eHonninForm(
      input(attr("type") := "radio", value := "1"), "本人",
      input(attr("type") := "radio", value := "0"), "家族",
    ),
    span("期限開始") -> eValidFrom.ele,
    span("期限終了") -> eValidUpto.ele
  )
  ele(cls := "shahokokuho-form")
