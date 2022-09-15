package dev.myclinic.scala.util

object Misc:
  def countPages(total: Int, itemsPerPage: Int): Int =
    (total + itemsPerPage - 1) / itemsPerPage

  object HokenUtil:
    def isValidTodoufukenBangou(bangou: Int): Boolean =
      bangou >= 1 && bangou <= 47

    def calcCheckingDigit(numberWithoutCheckingDigitArg: Int): Int =
      var numberWithoutCheckingDigit = numberWithoutCheckingDigitArg
      if (numberWithoutCheckingDigit < 0) {
        throw new RuntimeException("Negative number for calcCheckingDigit.")
      }
      var s = 0
      var m = 2
      while (numberWithoutCheckingDigit > 0) {
        val d = numberWithoutCheckingDigit % 10
        var dm = d * m
        if (dm >= 10) {
          dm = (dm / 10) + (dm % 10)
        }
        s += dm
        numberWithoutCheckingDigit /= 10
        if (m == 2) {
          m = 1
        } else {
          m = 2
        }
      }
      s = s % 10
      var v = 10 - s
      if (v == 10) {
        v = 0
      }
      v

    def hasValidCheckingDigit(bangou: Int): Boolean =
      val v = calcCheckingDigit(bangou / 10)
      v == (bangou % 10)
