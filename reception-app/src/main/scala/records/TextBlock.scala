package dev.myclinic.scala.web.reception.records

import dev.myclinic.scala.model.*
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.kanjidate.DateUtil
import java.time.{LocalDate, LocalDateTime}
import org.scalajs.dom.{HTMLElement}

class TextBlock(text: Text):
  val ele = div(innerText := text.content)