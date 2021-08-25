package dev.myclinic.scala.model

import io.circe._

import java.time.LocalTime
import java.time.format.DateTimeFormatter

object JsonCodecs {
  private val timeFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("HH:mm:ss")

  implicit val timeEncoder: Encoder[LocalTime] = (t: LocalTime) => 
    Json.fromString(t.format(timeFormatter))
  
  implicit val timeDecoder: Decoder[LocalTime] = (c: HCursor) =>
    for(
      s <- c.as[String]
    ) yield LocalTime.parse(s, timeFormatter)
}