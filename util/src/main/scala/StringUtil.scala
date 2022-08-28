package dev.myclinic.scala.util

import scala.util.matching.Regex

object StringUtil:
  def cut(startPattern: Regex, src: String): (String, List[String]) =
    val starts: List[Int] =
      startPattern.findAllMatchIn(src).toList.map(_.start)
    val ends = starts.drop(1) :+ src.size
    val pre: String = starts.headOption.fold("")(src.substring(0, _))
    val chunks = starts.zip(ends).map { case (start, end) =>
      src.substring(start, end).strip
    }
    (pre, chunks)

  def classify[T, U](
      pat: Regex,
      src: String,
      matched: String => T,
      unmatched: String => U
  ): List[T | U] =
    val startEnds: List[(Int, Int)] =
      pat.findAllMatchIn(src).toList.map(m => (m.start, m.end))
    val (last, list) = startEnds.foldLeft((0, List.empty[T | U]))((acc, item) =>
      (acc, item) match {
        case ((prevPos, list), (start, end)) =>
          val us: String = src.substring(prevPos, start)
          val ms: String = src.substring(start, end)
          (end, list :+ unmatched(us) :+ matched(ms))
      }
    )
    list :+ unmatched(src.substring(last, src.size))

  def collectIndexIter(
      t: String,
      start: Int,
      searchText: String,
      acc: List[Int]
  ): List[Int] =
    val i = t.indexOf(searchText, start)
    if i < 0 then acc
    else collectIndexIter(t, i + searchText.size, searchText, acc :+ i)

  def collectIndex(t: String, searchText: String): List[Int] =
    collectIndexIter(t, 0, searchText, List.empty)

  def splitWithIter[T](
      t: String,
      start: Int,
      sep: String,
      otherHandler: String => T,
      matchHandler: String => T,
      acc: List[T]
  ): List[T] =
    val isep: Int = t.indexOf(sep, start)
    if isep < 0 then
      val os: String = t.substring(start, t.size)
      if os.isEmpty then acc else acc :+ otherHandler(os)
    else
      val os: String = t.substring(start, isep)
      val ms: String = t.substring(isep, isep + sep.size)
      splitWithIter(
        t,
        isep + sep.size,
        sep,
        otherHandler,
        matchHandler,
        acc ++ (
          if os.isEmpty then List.empty else List(otherHandler(os))
        ) :+ matchHandler(ms)
      )

  def splitWith[T](
      t: String,
      sep: String,
      otherHandler: String => T,
      matchHandler: String => T
  ): List[T] =
    splitWithIter(t, 0, sep, otherHandler, matchHandler, List.empty)
