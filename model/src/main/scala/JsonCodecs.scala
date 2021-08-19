package dev.myclinic.scala.model

import java.time.{LocalDate, LocalTime}
import java.time.format.DateTimeFormatter
import io.circe._

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