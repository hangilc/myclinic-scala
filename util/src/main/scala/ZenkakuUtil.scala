package dev.myclinic.scala.util

import scala.collection.immutable.NumericRange

object ZenkakuUtil:
  val alphaToZenkakuMap: Map[Char, Char] = 
    rangeMap('0' to '9', '０')
      ++ rangeMap('a' to 'z', 'ａ')
      ++ rangeMap('A' to 'Z', 'Ａ')
      ++ Map(
    '.' -> '．',
    ' ' -> '　',
    '-' -> 'ー',
    '(' -> '（',
    ')' -> '）',
    ',' -> '、',
    '%' -> '％'
  )

  def rangeMap(range: NumericRange[Char], valueStart: Char): Map[Char, Char] = 
    Map.from(range.toList.map(_.toChar).zipWithIndex.map {
      case (a, i) => a -> (valueStart + i).toChar
    })

  val zenkakuToAlphaMap = alphaToZenkakuMap.map(kv => (kv._2, kv._1))

  def toZenkakuChar(ch: Char): Char = alphaToZenkakuMap.getOrElse(ch, ch)
  def toHankakuChar(ch: Char): Char = zenkakuToAlphaMap.getOrElse(ch, ch)

  def toZenkaku(s: String): String = s.map(toZenkakuChar _)
  def toHankaku(s: String): String = s.map(toHankakuChar _)

  def toZenkakuCharExcluding(excludes: Set[Char]): Char => Char =
    c => if excludes.contains(c) then c else toZenkakuChar(c)
  def toZenkakuCharExcluding(excludes: Char*): Char => Char =
    toZenkakuCharExcluding(excludes.toSet)
  def toZenkakuWithException(s: String, excludes: Set[Char]): String =
    s.map(toZenkakuCharExcluding(excludes))

  def toZenkakuFun(pred: Char => Boolean): String => String =
    val map = alphaToZenkakuMap.view.filterKeys(pred)
    s => s.map(c => map.getOrElse(c, c))

  def toHankakuFun(pred: Char => Boolean): String => String =
    val map = zenkakuToAlphaMap.view.filterKeys(pred)
    s => s.map(c => map.getOrElse(c, c))

  val convertToZenkakuDigits: String => String =
    toZenkakuFun(c => c >= '0' && c <= '9')
