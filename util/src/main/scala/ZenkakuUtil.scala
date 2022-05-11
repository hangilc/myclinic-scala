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
  ) ++ Map.from(
    ('a' to 'z').toList.zipWithIndex.map {
      case (a, i) => {
        a -> ('ａ' + i).toChar
      }
    }
  ) ++ Map.from(
    ('A' to 'Z').toList.zipWithIndex.map {
      case (a, i) => {
        a -> ('Ａ' + i).toChar
      }
    }
  )

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
