package dev.myclinic.scala.util.holidayjp

import dev.myclinic.scala.util.DateUtil
import java.time.DayOfWeek.*
import java.time.*
import java.time.temporal.ChronoUnit.*
import scala.collection.mutable.ListBuffer

case class NationalHoliday(date: LocalDate, name: String):
  override def toString(): String =
    s"${date}（${DateUtil.youbi(date)}） ${name}"

object NationalHoliday:
  def apply(year: Int, month: Int, day: Int, name: String): NationalHoliday =
    NationalHoliday(LocalDate.of(year, month, day), name)

private case class HolidayList(year: Int, list: ListBuffer[NationalHoliday]):
  def get(): List[NationalHoliday] = list.sortBy(_.date).toList

  def contains(d: LocalDate): Boolean = list.find(_.date == d).isDefined

  def add(holiday: NationalHoliday): Unit = list.append(holiday)

  def add(date: LocalDate, name: String): Unit =
    list.append(NationalHoliday(date, name))

  def add(month: Int, day: Int, name: String): Unit =
    list.append(NationalHoliday(LocalDate.of(year, month, day), name))

  def nthMonday(month: Int, nthOneBased: Int, name: String): Unit =
    val d = DateUtil.nthDayOfWeek(year, month, MONDAY, nthOneBased)
    add(d, name)

  def populate(modifiers: Modifier*): HolidayList =
    modifiers.foreach(_.addTo(this))
    this

private object HolidayList:
  def apply(year: Int): HolidayList =
    HolidayList(year, ListBuffer[NationalHoliday]())

private trait Modifier:
  def addTo(list: HolidayList): Unit
  def discard(): Modifier = ModifierNop()

private class ModifierNop extends Modifier:
  def addTo(list: HolidayList): Unit = ()

private class ModifierOne(val holiday: HolidayList => Option[NationalHoliday])
    extends Modifier:
  def addTo(list: HolidayList): Unit =
    holiday(list) match {
      case Some(h) => list.add(h)
      case None    => ()
    }

  def moveTo(month: Int, day: Int): ModifierOne =
    ModifierOne(list =>
      holiday(list) map (h => {
        val d = LocalDate.of(h.date.getYear, month, day)
        h.copy(date = d)
      })
    )

def at(date: LocalDate, name: String): ModifierOne =
  ModifierOne(list => Some(NationalHoliday(date, name)))

def at(month: Int, day: Int, name: String): ModifierOne =
  ModifierOne(list =>
    Some(NationalHoliday(LocalDate.of(list.year, month, day), name))
  )

def nthMonday(month: Int, nthOneBased: Int, name: String): ModifierOne =
  ModifierOne(list =>
    Some(
      NationalHoliday(
        DateUtil.nthDayOfWeek(list.year, month, MONDAY, nthOneBased),
        name
      )
    )
  )

def secondMonday(month: Int, name: String): ModifierOne =
  nthMonday(month, 2, name)

def thirdMonday(month: Int, name: String): ModifierOne =
  nthMonday(month, 3, name)

def dependsOnYear(f: Int => ModifierOne): ModifierOne =
  ModifierOne(list => f(list.year).holiday(list))

val none = ModifierOne(_ => None)

def range(
    fromMonth: Int,
    fromDay: Int,
    uptoMonth: Int,
    uptoDay: Int,
    name: String
): Modifier = new Modifier:
  def addTo(hl: HolidayList): Unit =
    val year = hl.year
    val fromDate = LocalDate.of(year, fromMonth, fromDay)
    val uptoDate = LocalDate.of(year, uptoMonth, uptoDay)
    var d = fromDate
    import math.Ordering.Implicits.infixOrderingOps
    while d <= uptoDate do ()

private val ganjitsu: Modifier = at(1, 1, "元日")

private val seijin: Modifier = secondMonday(1, "成人の日")

private val kenkoku: Modifier = dependsOnYear(year => {
  if year >= 1967 then at(2, 11, "建国記念の日")
  else none
})

private val tennou: Modifier = at(2, 23, "天皇誕生日")

private val shunbun: Modifier = dependsOnYear(year => {
  at(DateUtil.shunbun(year), "春分の日")
})

private val shouwa: Modifier = at(4, 29, "昭和の日")

private val kenpou: Modifier = at(5, 3, "憲法記念日")
private val midori: Modifier = at(5, 4, "みどりの日")
private val kodomo: Modifier = at(5, 5, "こどもの日")
private val uminohi: Modifier = dependsOnYear(year => {
  val name = "海の日"
  year match {
    case 2021 => at(7, 22, name)
    case _    => thirdMonday(7, name)
  }
})
private val yamanohi: Modifier = dependsOnYear(year => {
  val name = "山の日"
  year match {
    case 2021 => at(8, 8, name)
    case _    => at(8, 11, name)
  }
})
private val keirou: Modifier = thirdMonday(9, "敬老の日")
private val shuubun: Modifier =
  dependsOnYear(year => at(DateUtil.shuubun(year), "秋分の日"))
private val sports: Modifier = dependsOnYear(year => {
  val name = "スポーツの日"
  year match {
    case 2021 => at(7, 23, name)
    case _    => secondMonday(10, name)
  }
})
private val bunka: Modifier = at(11, 3, "文化の日")
private val kinrou: Modifier = at(11, 23, "勤労感謝の日")

private val furikae: Modifier = new Modifier:
  def addTo(hl: HolidayList): Unit =
    def isSunday(d: LocalDate): Boolean = d.getDayOfWeek == SUNDAY
    def pickFurikae(d: LocalDate): LocalDate =
      if hl.contains(d) then pickFurikae(d.plus(1, DAYS)) else d
    val furikae = ListBuffer[LocalDate]()
    hl.list.foreach(h => {
      if isSunday(h.date) then furikae.append(pickFurikae(h.date.plus(1, DAYS)))
    })
    furikae.foreach(d => hl.add(d, "振替休日"))

private val sandwiched: Modifier = new Modifier:
  def addTo(hl: HolidayList): Unit =
    val hs = ListBuffer[LocalDate]()
    hl.list.foreach(h => {
      val d1 = h.date.plus(1, DAYS)
      val d2 = h.date.plus(2, DAYS)
      if hl.contains(d2) && !hl.contains(d1) then hs.append(d1)
    })
    hs.foreach(hl.add(_, "休日"))

def nationalHollidays(year: Int): HolidayList =
  HolidayList(year)
    .populate(
      ganjitsu,
      seijin,
      kenkoku,
      tennou,
      shunbun,
      shouwa,
      kenpou,
      midori,
      kodomo,
      uminohi,
      yamanohi,
      keirou,
      shuubun,
      sports,
      bunka,
      kinrou,
      sandwiched,
      furikae
    )

object HolidayJp:
  def listHolidays(year: Int): List[NationalHoliday] =
    nationalHollidays(year).get()
