package dev.myclinic.scala.web.appbase

import dev.fujiwara.domq.prop.*
import dev.myclinic.scala.model.Shahokokuho
import dev.myclinic.scala.model.ValidUpto
import dev.myclinic.scala.web.appbase.ShahokokuhoValidator.{*, given}
import dev.myclinic.scala.web.appbase.ShahokokuhoValidator
import dev.fujiwara.kanjidate.KanjiDate
import PropUtil.*
import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.validator.section.Implicits.*
import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions

case class ShahokokuhoProps(model: Option[Shahokokuho]):
  val props = (
    Prop[Shahokokuho, HokenshaBangouError.type, Int](
      "保険者番号",
      TextInput(
        _.hokenshaBangou.toString,
        HokenshaBangouValidator.validateInput _,
        _(cls := "hokensha-bangou-input")
      ),
      SpanDisp(_.hokenshaBangou.toString)
    ),
    Prop[Shahokokuho, HihokenshaKigouError.type, String](
      "被保険者記号",
      TextInput(
        _.hihokenshaKigou,
        HihokenshaKigouValidator.validate _,
        _(cls := "hihokensha-kigou-input")
      ),
      SpanDisp(_.hihokenshaKigou)
    ),
    Prop[Shahokokuho, HihokenshaBangouError.type, String](
      "被保険者番号",
      TextInput(
        _.hihokenshaBangou,
        HihokenshaBangouValidator.validate _,
        _(cls := "hihokensha-bangou-input")
      ),
      SpanDisp(_.hihokenshaBangou)
    ),
    Prop[Shahokokuho, HonninError.type, Int](
      "本人・家族",
      RadioInput(
        List("本人" -> 1, "家族" -> 0),
        0,
        _.honninStore,
        HonninValidator.validate
      ),
      SpanDisp(
        m => Map(1 -> "本人", 0 -> "家族")(m.honninStore)
      )
    ),
    Prop[Shahokokuho, ValidFromError.type, LocalDate](
      "期限開始",
      DateInput(
        _.validFrom,
        ValidFromValidator.validateOption _,
      ),
      SpanDisp(
        m => KanjiDate.dateToKanji(m.validFrom),
      )
    ),
    Prop[Shahokokuho, ValidUptoError.type, ValidUpto](
      "期限終了",
      ValidUptoInput(
        _.validUpto,
        ValidUptoValidator.validate _
      ),
      ValidUptoDisp(
        _.validUpto
      )
    )
  )
