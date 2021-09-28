package dev.myclinic.scala.model

import org.scalatest.funsuite.AnyFunSuite
import java.time.*

class AppointTimeTest extends AnyFunSuite:
  def time(hour: Int, minute: Int): LocalTime = LocalTime.of(hour, minute, 0)
  def date(month: Int, day: Int): LocalDate = LocalDate.of(2020, month, day)

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
      AppointTime(0, 0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, 0, date(10, 1), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats) === true)

    val ats2 = List(
      AppointTime(0, 0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, 0, date(10, 2), time(10, 20), time(10, 40), "regular", 1),
      AppointTime(0, 0, date(10, 1), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats2) === true)
  }

  test("AppointTime.overlaps does not detect false overlap"){
    val ats = List(
      AppointTime(0, 0, date(10, 1), time(10, 0), time(10, 40), "regular", 1),
      AppointTime(0, 0, date(10, 2), time(10, 20), time(10, 40), "regular", 1),
    )
    assert(AppointTime.overlaps(ats) === false)
  }


