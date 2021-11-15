package dev.myclinic.scala.db

import doobie.util.{Get, Put}

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

import dev.myclinic.scala.model.{Sex, WaitState}
import java.time.LocalDateTime

private object LocalDateMapping:
  private val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd")

  def fromString(s: String): LocalDate =
    LocalDate.parse(s, sqlDateFormatter)

  def toString(d: LocalDate): String =
    d.format(sqlDateFormatter)

private object LocalTimeMapping:
  private val sqlTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  def fromString(s: String): LocalTime =
    LocalTime.parse(s, sqlTimeFormatter)

  def toString(t: LocalTime): String =
    t.format(sqlTimeFormatter)

private object LocalDateTimeMapping:
  val sqlDateTimeFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

  def fromString(s: String): LocalDateTime = LocalDateTime.parse(s, sqlDateTimeFormatter)
  def toString(dt: LocalDateTime): String = dt.format(sqlDateTimeFormatter)

private object SexMapping:
  def fromString(s: String): Sex = s match
    case "M" => Sex.Male
    case "F" => Sex.Female
    case _ => throw new RuntimeException("Unknown sex: " + s)

  def toString(s: Sex): String = s match
    case Sex.Male => "M"
    case Sex.Female => "F"

object DoobieMapping:
  implicit val localDateGet: Get[LocalDate] =
    Get[String].map(LocalDateMapping.fromString _)

  implicit val localDateSet: Put[LocalDate] = 
    Put[String].tcontramap(LocalDateMapping.toString _)
  
  implicit val localTimeGet: Get[LocalTime] =
    Get[String].map(LocalTimeMapping.fromString _)
  
  implicit val localTimeSet: Put[LocalTime] = 
    Put[String].tcontramap(LocalTimeMapping.toString _)
  
  implicit val localDateTimeGet: Get[LocalDateTime] = 
    Get[String].map(LocalDateTimeMapping.fromString)

  implicit val localDateTimeSet: Put[LocalDateTime] = 
    Put[String].tcontramap(LocalDateTimeMapping.toString _)

  implicit val sexGet: Get[Sex] = Get[String].map(SexMapping.fromString _)
  implicit val sexSet: Put[Sex] = Put[String].tcontramap(SexMapping.toString _)

  implicit val optionStringGet: Get[Option[String]] =
    Get[String].map(str => if str == null then None else Some(str))
  implicit val optionStringSet: Put[Option[String]] =
    Put[String].tcontramap(opt => opt match {
      case Some(s) => s
      case None => null
    })

  implicit val waitStateGet: Get[WaitState] = Get[Int].map(WaitState.fromCode _)
  implicit val waitStatePut: Put[WaitState] = Put[Int].tcontramap(_.code)
