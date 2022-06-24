package dev.fujiwara.dateinput

import dev.fujiwara.domq.all.{*, given}
import scala.language.implicitConversions
import dev.fujiwara.kanjidate.KanjiDate.{Gengou, Seireki, Era, eraName}
import java.time.LocalDate
import dev.fujiwara.kanjidate.KanjiDate
import dev.fujiwara.validator.ValidatorUtil.*
import scala.util.Try
import scala.util.Success
import scala.util.Failure
import cats.syntax.all.*
import org.scalajs.dom.MouseEvent

class DateInputForm(
    var value: Option[LocalDate] = None,
    gengouList: List[Era] = DateInputForm.defaultGengouList
)(using initNoneConverter: InitNoneConverter):
  value = value.orElse(initNoneConverter.convert)
  val gengouSelect = SelectProxy(gengouList, (opt, e) => opt(eraName(e)))
  val nenInput = input
  val monthInput = input
  val dayInput = input
  val ele = div(cls := "domq-date-input-form domq-user-select-none",
    gengouSelect.ele(cls := "domq-date-input-form-gengou"),
    nenInput(cls := "domq-date-input-form-nen"),
    span("年", onclick := (doNenClick _), cls := "domq-cursor-pointer"),
    monthInput(cls := "domq-date-input-form-month"),
    span("月", onclick := (doMonthClick _), cls := "domq-cursor-pointer"),
    dayInput(cls := "domq-date-input-form-day"),
    span("日", onclick := (doDayClick _), cls := "domq-cursor-pointer")
  )
  updateUI()

  def validated: Either[String, Option[LocalDate]] =
    import DateInputFormValidator.*
    validateDateInputForm(
      Some(gengouSelect.selected),
      nenInput.value,
      monthInput.value,
      dayInput.value
    ).asEither

  def simulateChange(f: Option[LocalDate] => Option[LocalDate]): Unit =
     validated match {
      case Right(dOpt) =>
        value = f(dOpt)
        updateUI()
      case _ => ()
    }

  private def doNenClick(event: MouseEvent): Unit =
    val n: Int = if event.shiftKey then -1 else 1
    simulateChange(_.map(_.plusYears(n)))

  private def doMonthClick(event: MouseEvent): Unit =
    val n: Int = if event.shiftKey then -1 else 1
    simulateChange(_.map(_.plusMonths(n)))

  private def doDayClick(event: MouseEvent): Unit =
    val n: Int = if event.shiftKey then -1 else 1
    simulateChange(_.map(_.plusDays(n)))

  private def updateUI(): Unit =
    value match {
      case None =>
        nenInput.value = ""
        monthInput.value = ""
        dayInput.value = ""
      case Some(d) =>
        val di = KanjiDate.DateInfo(d)
        if !gengouSelect.contains(di.era) then gengouSelect.prepend(di.era)
        gengouSelect.simulateSelect(di.era)
        nenInput.value = di.nen.toString
        monthInput.value = di.month.toString
        dayInput.value = di.day.toString
    }

object DateInputForm:
  val defaultGengouList: List[Era] = 
    Gengou.values.toList.reverse

object DateInputFormValidator:
  import cats.data.Validated
  import cats.data.Validated.{Valid, Invalid}
  import dev.fujiwara.validator.ValidatorUtil.*

  abstract class SectionValidator[S](name: String):
    type Result[T] = Validated[List[(S, String)], T]

    def section: S

    def getName: String = name

    def error[T](msg: String): Result[T] = Invalid(List((section, msg)))

    def cond[T](
        test: Boolean,
        validValue: T,
        errMsg: => String
    ): Result[T] =
      if test then Valid(validValue) else error(errMsg)

    def isSome[T](src: Option[T]): Result[T] =
      src match {
        case Some(t) => Valid(t)
        case None    => error(s"${name}の値が設定されていません（None）。")
      }

    def isNotEmpty(src: String): Result[String] =
      cond(!(src == null || src.isEmpty), src, s"${name}の値が入力されていません。")

    def toInt(src: String): Result[Int] =
      src.toIntOption match {
        case Some(i) => Valid(i)
        case None    => error(s"${name}の入力が整数でありません。")
      }

    def isPositive(i: Int): Result[Int] =
      cond(i > 0, i, s"${name}の値が正の整数でありません。")

    def isInRange(i: Int, minVal: Int, maxVal: Int): Result[Int] =
      cond(
        i >= minVal && i <= maxVal,
        i,
        s"${name}の値（${i}）が、${minVal} と ${maxVal} の範囲内でありません。"
      )

    def validateAsPositiveInt(i: Int): Result[Int] =
      isPositive(i)

    def validateAsPositiveInt(src: String): Result[Int] =
      isNotEmpty(src)
        .andThen(toInt(_))
        .andThen(isPositive(_))

    def validateAsInRange(src: String, minVal: Int, maxVal: Int): Result[Int] =
      isNotEmpty(src)
        .andThen(toInt(_))
        .andThen(isInRange(_, minVal, maxVal))

  extension [S, T] (v: Validated[List[(S, String)], T])
    def asEither: Either[String, T] =
      v.toEither.left.map(errs => errs.map(_._2).mkString("\n"))

  enum SectionGroup(name: String)
      extends SectionValidator[SectionGroup](name):
    def section: SectionGroup = this
    case GengouSection extends SectionGroup("元号")
    case NenSection extends SectionGroup("年")
    case MonthSection extends SectionGroup("月")
    case DaySection extends SectionGroup("日")
    case InvalidValueSection extends SectionGroup("指定されて日付が無効な日付です。")

  type Result[T] = SectionGroup#Result[T]
  import SectionGroup.*

  def validateGengou(gOpt: Option[Era]): Result[Era] =
    GengouSection.isSome(gOpt)

  def validateNen(src: String): Result[Int] =
    NenSection.validateAsPositiveInt(src)

  def validateMonth(src: String): Result[Int] =
    MonthSection.validateAsInRange(src, 1, 12)

  def validateDay(src: String): Result[Int] =
    DaySection.validateAsInRange(src, 1, 31)

  def validateValue(e: Era, nen: Int, month: Int, day: Int): Result[LocalDate] =
    val year = KanjiDate.eraToYear(e, nen)
    Try(LocalDate.of(year, month, day)) match {
      case Success(d) => Valid(d)
      case Failure(_) => InvalidValueSection.error(InvalidValueSection.getName)
    }

  def validateDateInputForm(
      era: Option[Era],
      nen: String,
      month: String,
      day: String
  ): Result[Option[LocalDate]] =
    if nen.isEmpty && month.isEmpty && day.isEmpty then Valid(None)
    else
      (
        validateGengou(era),
        validateNen(nen),
        validateMonth(month),
        validateDay(day)
      )
        .tupled
        .andThen {
          case (g, n, m, d) => validateValue(g, n, m, d)
        }
        .map(Some(_))
