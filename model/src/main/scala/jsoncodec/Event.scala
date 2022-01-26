package dev.myclinic.scala.model.jsoncodec

import dev.myclinic.scala.model.{AppEvent, HotlineBeep}
import io.circe.*
import io.circe.syntax.*

type EventType = AppEvent | HotlineBeep

trait Event extends AppEventCodec:
  given Decoder[EventType] with
    def apply(c: HCursor): Decoder.Result[EventType] =
      for
        format <- c.downField("format").as[String]
        event <- format match {
          case "appevent" => c.downField("data").as[AppEvent]
        }
      yield event.asInstanceOf[EventType]

  given Encoder[EventType] with
    def apply(event: EventType): Json =
      event match {
        case appEvent @ _: AppEvent => serialize("appevent", appEvent)
        case hotlineBeep @ _: HotlineBeep => serialize("hotline-beep", hotlineBeep)
      }
    
    def serialize[T](format: String, data: T)(using Encoder[T]): Json =
      Json.obj(
        "format" -> Json.fromString(format),
        "data" -> data.asJson
      )
