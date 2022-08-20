package dev.fujiwara.scala.drawer

import io.circe.*
import io.circe.syntax.*
import io.circe.generic.semiauto.*

case class PrintRequest(
  setup: List[Op],
  pages: List[List[Op]]
)

object PrintRequest:
  given Encoder[PrintRequest] = deriveEncoder[PrintRequest]
  given Decoder[PrintRequest] = deriveDecoder[PrintRequest]
