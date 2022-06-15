package dev.fujiwara.dateinput

import dev.fujiwara.kanjidate.KanjiDate.DateInfo
import java.time.LocalDate

trait DateFormatConfig:
  def nenFormatter(d: DateInfo): String
  def monthFormatter(d: DateInfo): String
  def dayFormatter(d: DateInfo): String

  def format(date: LocalDate): String =
    val info = DateInfo(date)
    s"${nenFormatter(info)}${monthFormatter(info)}${dayFormatter(info)}"

object DateFormatConfig:
  given defaultConfig: DateFormatConfig = new DateFormatConfig:
    def nenFormatter(d: DateInfo): String = s"${d.gengou}${d.nen}年"
    def monthFormatter(d: DateInfo): String = s"${d.month}月"
    def dayFormatter(d: DateInfo): String = s"${d.day}日"
