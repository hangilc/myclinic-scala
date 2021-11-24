package dev.myclinic.scala.util

import java.time.LocalDate

object RcptUtil:
  def calcRcptAge(
      bdYear: Int,
      bdMonth: Int,
      bdDay: Int,
      atYear: Int,
      atMonth: Int
  ): Int =
    var age: Int = atYear - bdYear
    if (atMonth < bdMonth) then age -= 1
    else if (atMonth == bdMonth) then if (bdDay != 1) then age -= 1
    age

  def calcRcptAge(birthday: LocalDate, at: LocalDate): Int =
    calcRcptAge(
      birthday.getYear,
      birthday.getMonthValue,
      birthday.getDayOfMonth,
      at.getYear,
      at.getMonthValue
    )

  def calcCharge(ten: Int, futanWari: Int): Int =
    var c = ten * futanWari
    val r = c % 10
    if (r < 5) then c -= r
    else c += (10 - r)
    c
