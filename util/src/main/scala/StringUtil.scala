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

  def collectIndexIter(t: String, start: Int, searchText: String, acc: List[Int]): List[Int] =
    val i = t.indexOf(searchText, start)
    if i < 0 then acc
    else collectIndexIter(t, i + searchText.size, searchText, acc :+ i)

  def collectIndex(t: String, searchText: String): List[Int] =
    collectIndexIter(t, 0, searchText, List.empty)

