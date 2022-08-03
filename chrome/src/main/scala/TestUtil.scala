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