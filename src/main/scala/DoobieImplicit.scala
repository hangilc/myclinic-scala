package dev.myclinic.scala.doobie

import doobie.util.Get

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import dev.myclinic.scala.model.{Sex, Male, Female}

object LocalDateMapping {
  private val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd");

  def fromString(s: String): LocalDate = {
    LocalDate.parse(s, sqlDateFormatter)
  }
}

object SexMapping {
  def fromString(s: String): Sex = s match {
    case "M" => Male
    case "F" => Female
    case _ => throw new RuntimeException("Invalid sex: " + s)
  }
}

object DoobieMapping {
  implicit val localDateGet: Get[LocalDate] =
    Get[String].map(LocalDateMapping.fromString _)

  implicit val sexGet: Get[Sex] = Get[String].map(SexMapping.fromString _)
}
