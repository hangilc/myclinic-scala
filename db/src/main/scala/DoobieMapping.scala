package dev.myclinic.scala.db

import doobie.util.{Get, Put}

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import dev.myclinic.scala.model.Sex

private object LocalDateMapping {
  private val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd")

  def fromString(s: String): LocalDate = {
    LocalDate.parse(s, sqlDateFormatter)
  }

  def toString(d: LocalDate): String = {
    d.format(sqlDateFormatter)
  }
}

private object LocalTimeMapping {
  private val sqlTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  def fromString(s: String): LocalTime = {
    LocalTime.parse(s, sqlTimeFormatter)
  }

  def toString(t: LocalTime): String = {
    t.format(sqlTimeFormatter)
  }
}

private object SexMapping {
  def fromString(s: String): Sex = s match {
    case "M" => Sex.Male
    case "F" => Sex.Female
    case _ => throw new RuntimeException("Unknown sex: " + s)
  }

  def toString(s: Sex): String = s match {
    case Sex.Male => "M"
    case Sex.Female => "F"
  }
}

object DoobieMapping {
  implicit val localDateGet: Get[LocalDate] =
    Get[String].map(LocalDateMapping.fromString _)

  implicit val localDateSet: Put[LocalDate] = 
    Put[String].tcontramap(LocalDateMapping.toString _)
  
  implicit val localTimeGet: Get[LocalTime] =
    Get[String].map(LocalTimeMapping.fromString _)
  
  implicit val localTimeSet: Put[LocalTime] = 
    Put[String].tcontramap(LocalTimeMapping.toString _)

  implicit val sexGet: Get[Sex] = Get[String].map(SexMapping.fromString _)
  implicit val sexSet: Put[Sex] = Put[String].tcontramap(SexMapping.toString _)
}
