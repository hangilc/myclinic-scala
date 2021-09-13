package dev.myclinic.scala.modeljson

import io.circe.Decoder
import io.circe.Encoder

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import scala.util.Try

trait DateTime {
  val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd")

  val sqlTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  val sqlDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

  given Encoder[LocalDate] = Encoder.encodeString.contramap(_.toString())

  given Decoder[LocalDate] = Decoder.decodeString.emapTry(str =>
    Try(LocalDate.parse(str, sqlDateFormatter))
  )

  given Encoder[LocalTime] =
    Encoder.encodeString.contramap(_.format(sqlTimeFormatter))

  given Decoder[LocalTime] = Decoder.decodeString.emapTry(str =>
    Try(LocalTime.parse(str, sqlTimeFormatter))
  )

  given Encoder[LocalDateTime] =
    Encoder.encodeString.contramap(_.format(sqlDateTimeFormatter))

  given Decoder[LocalDateTime] = Decoder.decodeString.emapTry(str =>
    Try(LocalDateTime.parse(str, sqlTimeFormatter))
  )
}
