package dev.myclinic.scala.model.jsoncodec

import io.circe.Decoder
import io.circe.Encoder
import io.circe.KeyEncoder
import io.circe.KeyDecoder

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import scala.util.Try

trait DateTime:
  val sqlDateFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd")

  val sqlTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  val sqlDateTimeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss")

  implicit val dateEncoder: Encoder[LocalDate] = Encoder.encodeString.contramap(_.toString())

  implicit val dateDecoder: Decoder[LocalDate] = Decoder.decodeString.emapTry(str =>
    Try(LocalDate.parse(str, sqlDateFormatter))
  )

  implicit val timeEncoder: Encoder[LocalTime] =
    Encoder.encodeString.contramap(_.format(sqlTimeFormatter))

  implicit val timeDecoder: Decoder[LocalTime] = Decoder.decodeString.emapTry(str =>
    Try(LocalTime.parse(str, sqlTimeFormatter))
  )

  implicit val dateTimeEncoder: Encoder[LocalDateTime] =
    Encoder.encodeString.contramap(_.format(sqlDateTimeFormatter))

  implicit val dateTimeDecoder: Decoder[LocalDateTime] = Decoder.decodeString.emapTry(str =>
    Try(LocalDateTime.parse(str, sqlDateTimeFormatter))
  )

  implicit val dateKeyEncoder: KeyEncoder[LocalDate] = new KeyEncoder {
    override def apply(date: LocalDate): String = date.toString
  }

  implicit val dateKeyDecoder: KeyDecoder[LocalDate] = new KeyDecoder {
    override def apply(key: String): Option[LocalDate] = 
      Try(LocalDate.parse(key, sqlDateFormatter)).toOption
  }