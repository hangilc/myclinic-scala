package dev.myclinic.scala.util

object ZenkakuUtil:
  val zenkakuDigitToDigit: Char => Char = c =>
    c match {
      case '０' => '0'
      case '１' => '1'
      case '２' => '2'
      case '３' => '3'
      case '４' => '4'
      case '５' => '5'
      case '６' => '6'
      case '７' => '7'
      case '８' => '8'
      case '９' => '9'
      case _   => c
    }

  extension (f: Char => Char)
    def <+>(g: Char => Char): Char => Char = (c: Char) =>
      val d = f(c)
      if d != c then d
      else g(c)

  def convertChars(src: String, f: Char => Char): String =
    src.toList.map(f).mkString("")

  def convertZenkakuDigits(src: String): String =
    convertChars(src, zenkakuDigitToDigit)