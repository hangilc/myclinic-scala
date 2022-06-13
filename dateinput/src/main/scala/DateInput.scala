package dev.fujiwara.dateinput

import java.time.LocalDate
import org.scalajs.dom.HTMLElement
import dev.fujiwara.domq.ElementQ.{*, given}
import dev.fujiwara.domq.Html.{*, given}
import dev.fujiwara.domq.Modifiers.{*, given}
import dev.fujiwara.kanjidate.KanjiDate.DateInfo


case class DateInput(init: LocalDate)(using config: DateInputConfig):
  val d = DateInfo(init)
  val ele = config.wrapper(cls := config.cssPrefix,
    span(config.nenFormatter(d), cls := config.cssClass("nen")),
    span(config.monthFormatter(d), cls := config.cssClass("month")),
    span(config.dayFormatter(d), cls := config.cssClass("day")),
  )

object DateInput:
  given defaultConfig: DateInputConfig = new DateInputConfig:
    def wrapper: HTMLElement = div
    def nenFormatter(d: DateInfo): String = s"${d.gengou}${d.nen}年"
    def monthFormatter(d: DateInfo): String = s"${d.month}月"
    def dayFormatter(d: DateInfo): String = s"${d.day}日"
    val cssPrefix: String = "dateinput"

trait DateInputConfig:
  def wrapper: HTMLElement
  def nenFormatter(d: DateInfo): String
  def monthFormatter(d: DateInfo): String
  def dayFormatter(d: DateInfo): String
  def cssPrefix: String

  def cssClass(ident: String): String = s"${cssPrefix}-${ident}"

