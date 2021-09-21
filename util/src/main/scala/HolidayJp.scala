package dev.myclinic.scala.util.holidayjp

import dev.myclinic.scala.util.DateUtil
import java.time.DayOfWeek.*
import java.time.*
import scala.collection.mutable.ListBuffer

case class Holiday(date: LocalDate, name: String):
  override def toString(): String =
    s"${date} ${name}"

object Holiday:
  def apply(year: Int, month: Int, day: Int, name: String): Holiday =
    Holiday(LocalDate.of(year, month, day), name)
  
private case class HolidayList(year: Int, list: ListBuffer[Holiday]):
  def get(): List[Holiday] = list.toList

  def add(date: LocalDate, name: String): Unit =
    list.append(Holiday(date, name))

  def add(month: Int, day: Int, name: String): Unit =
    list.append(Holiday(LocalDate.of(year, month, day), name))

  def addNthMonday(month: Int, nthOneBased: Int, name: String): Unit =
    val d = DateUtil.nthDayOfWeek(year, month, MONDAY, nthOneBased)
    add(d, name)

  def populate(modifiers: Modifier*): HolidayList =
    modifiers.foreach(_.modify(this))
    this

private object HolidayList:
  def apply(year: Int): HolidayList = HolidayList(year, ListBuffer[Holiday]())

private trait Modifier:
  def modify(hl: HolidayList): Unit

private val ganjitsu: Modifier = list => list.add(1, 1, "元日")

private val seijin: Modifier = list => list.addNthMonday(1, 2, "成人の日")

private val kenkoku: Modifier = list => list.add(2, 11, "建国記念の日")

private val tennou: Modifier = list => list.add(2, 23, "天皇誕生日")

private val shunbun: Modifier = list => 
  list.add(DateUtil.shunbun(list.year), "春分の日")

private val shouwa: Modifier = list => list.add(4, 29, "昭和の日")

private val kenpou: Modifier = list => list.add(5, 3, "憲法記念日")
private val midori: Modifier = list => list.add(5, 4, "みどりの日")
private val kodomo: Modifier = list => list.add(5, 5, "こどもの日")
private val uminohi: Modifier = list => 
  list.addNthMonday(7, 3, "海の日")
private val yamanohi: Modifier = list => list.add(8, 11, "山の日")

object HolidayJp:
  def listHolidays(year: Int): List[Holiday] =
    HolidayList(2021).populate(
      ganjitsu,
      seijin,
      tennou,
      shunbun,
      shouwa,
      kenpou,
      midori,
      kodomo,
      uminohi
    ).get()
