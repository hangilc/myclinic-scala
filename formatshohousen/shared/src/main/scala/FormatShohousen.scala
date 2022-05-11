package dev.myclinic.scala.formatshohousen

object FormatShohousen:
  val itemStartPattern = raw"(?m)^[0-9０-９]+[)）]".r

  def splitToParts(s: String): List[String] =
    val starts: List[Int] = itemStartPattern.findAllMatchIn(s).toList.map(_.start)
    val ends = starts.drop(1) :+ s.size
    starts.zip(ends).map {
      case (start, end) => s.substring(start, end).strip
    }
    
    