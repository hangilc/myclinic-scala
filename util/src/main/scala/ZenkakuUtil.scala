package dev.myclinic.scala.util

object ZenkakuUtil:
  val alphaToZenkakuMap: Map[Char, Char] = Map(
    '0' -> '０',
    '1' -> '１',
    '2' -> '２',
    '3' -> '３',
    '4' -> '４',
    '5' -> '５',
    '6' -> '６',
    '7' -> '７',
    '8' -> '８',
    '9' -> '９',
    '.' -> '．',
    ' ' -> '　',
    '-' -> 'ー',
    '(' -> '（',
    ')' -> '）',
    ',' -> '、',
    'g' -> 'ｇ',
    'm' -> 'ｍ',
  )

  val zenkakuToAlphaMap = alphaToZenkakuMap.map(kv => (kv._2, kv._1))

  def toZenkakuChar(ch: Char): Char = alphaToZenkakuMap.getOrElse(ch, ch)
  def toHankakuChar(ch: Char): Char = zenkakuToAlphaMap.getOrElse(ch, ch)

  def toZenkaku(s: String): String = s.map(toZenkakuChar _)
  def toHankaku(s: String): String = s.map(toHankakuChar _)

  def toZenkakuFun(pred: Char => Boolean): String => String =
    val map = alphaToZenkakuMap.view.filterKeys(pred)
    s => s.map(c => map.getOrElse(c, c))

  def toHankakuFun(pred: Char => Boolean): String => String =
    val map = zenkakuToAlphaMap.view.filterKeys(pred)
    s => s.map(c => map.getOrElse(c, c))


  val convertToZenkakuDigits: String => String = toZenkakuFun(c => c >= '0' && c <= '9')

