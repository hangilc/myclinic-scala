package dev.myclinic.scala.model

import org.scalatest.funsuite.AnyFunSuite
import java.time.*
import java.time.temporal.ChronoUnit.MINUTES

class AppointTimeTest extends AnyFunSuite:
  def time(hour: Int, minute: Int): LocalTime = LocalTime.of(hour, minute, 0)
  def date(month: Int, day: Int): LocalDate = LocalDate.of(2020, month, day)
  def appointTime(date: LocalDate, from: LocalTime, until: LocalTime): AppointTime =
    AppointTime(0, date, from, until, "regular", 1)
  def appointTime(date: LocalDate, from: LocalTime): AppointTime =
    AppointTime(0, date, from, from.plus(20, MINUTES), "regular", 1)

  test("AppointTime.timeIntervalOverlaps detects overlap"){
    val ints = List(
      (time(10, 0), time(10, 40)),
      (time(10, 20), time(10, 40))
    )
    assert(AppointTime.timeIntervalOverlaps(ints) === true)
  }

  test("AppointTime.timeIntervalOverlaps does not detect false overlap"){
    val ints = List(
      (time(10, 0), time(10, 20)),
      (time(10, 20), time(10, 40))
    )
    assert(AppointTime.timeIntervalOverlaps(ints) === false)
  }

  test("AppointTime.overlaps detects overlap"){
    val ats = List(
      AppointTime(0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, date(10, 1), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats) === true)

    val ats2 = List(
      AppointTime(0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, date(10, 2), time(10, 20), time(10, 40), "regular", 1),
      AppointTime(0, date(10, 1), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats2) === true)
  }

  test("AppointTime.overlaps does not detect false overlap"){
    val ats = List(
      AppointTime(0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, date(10, 2), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats) === false)
  }

  test("AppointTime.isAdjacentTo detects adjacent AppointTime"){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 1), time(10, 20), time(10, 40), "regular", 1)
    assert(a.isAdjacentTo(b))
  }

  test("AppointTime.isAdjacentTo detects non-adjacent AppointTime"){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 1), time(10, 40), time(11, 0), "regular", 1)
    assert(!a.isAdjacentTo(b))
  }

  test("AppointTime.isAdjacentTo detects different date."){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 2), time(10, 20), time(10, 40), "regular", 1)
    assert(!a.isAdjacentTo(b))
  }

  test("AppointTime.isAdjacentRun detects adjacent run."){
    val d = date(10, 1)
    assert(AppointTime.isAdjacentRun(List(
      appointTime(d, time(10, 0), time(10, 20)),
      appointTime(d, time(10, 20), time(10, 40)),
      appointTime(d, time(10, 40), time(11, 0)),
      appointTime(d, time(11, 0), time(11, 20)),
    )))
  }

  test("AppointTime.isAdjacentRun passes single appoint time list."){
    val d = date(10, 1)
    assert(AppointTime.isAdjacentRun(List(
      appointTime(d, time(10, 0), time(10, 20)),
    )))
  }

  test("AppointTime.isAdjacentRun detects non-adjacent run."){
    val d = date(10, 1)
    assert(!AppointTime.isAdjacentRun(List(
      appointTime(d, time(10, 0), time(10, 20)),
      appointTime(d, time(10, 20), time(10, 40)),
      appointTime(d, time(11, 0), time(11, 20)),
    )))
  }

  test("AppointTime.extractAdjacentRun extracts one element."){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 1), time(10, 40), time(10, 45), "regular", 1)
    val c = AppointTime(0, date(10, 1), time(10, 45), time(11, 50), "regular", 1)
    val list = List(a, b, c)
    assert(AppointTime.extractAdjacentRun(list) == (List(a), List(b, c)))
  }

  test("AppointTime.extractAdjacentRun extracts two elements."){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 1), time(10, 20), time(10, 40), "regular", 1)
    val c = AppointTime(0, date(10, 1), time(11, 0), time(11, 20), "regular", 1)
    val list = List(a, b, c)
    assert(AppointTime.extractAdjacentRun(list) == (List(a, b), List(c)))
  }

  test("AppointTime.extractAdjacentRun extracts three elements."){
    val a = AppointTime(0, date(10, 1), time(10, 0), time(10, 20), "regular", 1)
    val b = AppointTime(0, date(10, 1), time(10, 20), time(10, 40), "regular", 1)
    val c = AppointTime(0, date(10, 1), time(10, 40), time(11, 0), "regular", 1)
    val d = AppointTime(0, date(10, 1), time(14, 0), time(14, 20), "regular", 1)
    val list = List(a, b, c, d)
    assert(AppointTime.extractAdjacentRun(list) == (List(a, b, c), List(d)))
  }

  test("AppointTime.extractAdjacentRun test for empty list."){
    assert(AppointTime.extractAdjacentRun(List.empty) == (List.empty, List.empty))
  }


