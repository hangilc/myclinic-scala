package dev.myclinic.scala.model.jsoncodec

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._
import dev.myclinic.scala.model.WaitState
import scala.util.Try

trait WaitStateCodec:
  given Encoder[WaitState] with
    def apply(s: WaitState): Json = Json.fromInt(s.code)
  given Decoder[WaitState] = Decoder.decodeInt.emapTry(code =>
    Try(WaitState.fromCode(code))
  )
