package dev.myclinic.scala.model.jsoncodec

import dev.myclinic.scala.model.{AppEvent, HotlineBeep, EventIdNotice}
import io.circe.*
import io.circe.syntax.*

type EventType = AppEvent | HotlineBeep | EventIdNotice

trait Event extends AppEventCodec with Model:
  given Decoder[EventType] with
    def apply(c: HCursor): Decoder.Result[EventType] =
      for
        format <- c.downField("format").as[String]
        event <- format match {
          case "appevent" => c.downField("data").as[AppEvent]
          case "hotline-beep" => c.downField("data").as[HotlineBeep]
          case "event-id-notice" => c.downField("data").as[EventIdNotice]
        }
      yield event.asInstanceOf[EventType]

  given Encoder[EventType] with
    def apply(event: EventType): Json =
      event match {
        case appEvent @ _: AppEvent => serialize("appevent", appEvent)
        case hotlineBeep @ _: HotlineBeep => serialize("hotline-beep", hotlineBeep)
        case eventIdNotice @_: EventIdNotice => serialize("event-id-notice", eventIdNotice)
      }
    
    def serialize[T](format: String, data: T)(using Encoder[T]): Json =
      Json.obj(
        "format" -> Json.fromString(format),
        "data" -> data.asJson
      )
