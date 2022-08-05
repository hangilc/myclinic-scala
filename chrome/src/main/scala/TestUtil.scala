package dev.myclinic.scala.chrome

import scala.util.Random
import dev.myclinic.scala.model.Sex

object TestUtil:
  lazy val rand: Random = Random()

  def randomInt(from: Int, upto: Int): Int =
    rand.between(from, upto + 1)

  def randomSex: Sex =
    if rand.nextBoolean then Sex.Male else Sex.Female

  def randomString(len: Int): String =
    rand.alphanumeric.take(len).mkString

  def randomCharsOf(len: Int, chars: String): String =
    def nextChar: Char =
      val i = rand.nextInt(chars.length)
      chars.charAt(i)
    LazyList.continually(nextChar).take(len).mkString

  def randomDigits(len: Int): String =
    randomCharsOf(len, "0123456789")

  def randomPhone: String = 
    val num1 = "03"
    val num2 = randomDigits(4)
    val num3 = randomDigits(4)
    s"${num1}-${num2}-${num3}"

  val hirakana: String =
    "あいうえおかきくけこさしすせそたちつてとなにぬねのはひふへほまみむめもやゆよわをん"

  val katakana: String =
    "アイウエオカキクケコサシスセソタチツテトナニヌネノハヒフヘホマミムメモヤユヨワヲン"

  val japaneseComma = '、'

  val japanesePeriod = '。'

  val kanjiRange: (Int, Int) = (0x4e00, 0x9faf)

  val japaneseSpace = '　'

  val japaneseCharFreq: List[Double] = List(
    0.25, // Comma
    0.05, // Period
    0.25,  // Hirakana 
    0.05, // Katakana
    0 // Kanji
  )
  
  val japaneseCharDispatch: Vector[Double] = 
    japaneseCharFreq.foldLeft((0.0, Vector[Double]()))((acc, f) => 
      acc match {
        case (sum, vect) => 
          val newSum = sum + f
          (newSum, vect :+ newSum)
      }
    )._2.init :+ 1.0

  def nextHirakana: Char =
    val i = rand.nextInt(hirakana.length)
    hirakana.charAt(i)

  def nextKatakana: Char =
    val i = rand.nextInt(katakana.length)
    katakana.charAt(i)

  def nextKanji: Char =
    val code = rand.between(kanjiRange._1, kanjiRange._2 + 1)
    code.toChar

  def nextJapaneseChar: Char =
    val r1: Double = rand.nextDouble
    japaneseCharDispatch.indexWhere(_ >= r1) match {
      case 0 => japaneseComma
      case 1 => japanesePeriod
      case 2 => nextHirakana
      case 3 => nextKatakana
      case _ => nextKanji
    }

  




