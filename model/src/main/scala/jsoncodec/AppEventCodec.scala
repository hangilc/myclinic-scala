package dev.myclinic.scala.model.jsoncodec

import io.circe.*
import io.circe.syntax.*
import io.circe.parser.*
import io.circe.generic.semiauto._
import dev.myclinic.scala.model.*
import java.time.LocalDateTime

trait AppEventCodec extends Model with DateTime:

  given Encoder[AppEvent] = deriveEncoder[AppEvent]
  given Decoder[AppEvent] = deriveDecoder[AppEvent]

  given Encoder[AppModelEvent] with
    def apply(e: AppModelEvent): Json =
      e match {
        case AppointCreated(at, created) =>
          Json.obj(
            "model" -> Json.fromString("appoint"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "created" -> created.asJson
          )
        case AppointUpdated(at, updated) =>
          Json.obj(
            "model" -> Json.fromString("appoint"),
            "kind" -> Json.fromString("updated"),
            "createdAt" -> at.asJson,
            "updated" -> updated.asJson
          )
        case AppointDeleted(at, deleted) =>
          Json.obj(
            "model" -> Json.fromString("appoint"),
            "kind" -> Json.fromString("deleted"),
            "createdAt" -> at.asJson,
            "deleted" -> deleted.asJson
          )
        case AppointTimeCreated(at, created) =>
          Json.obj(
            "model" -> Json.fromString("appoint-time"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "created" -> created.asJson
          )
        case AppointTimeUpdated(at, updated) =>
          Json.obj(
            "model" -> Json.fromString("appoint-time"),
            "kind" -> Json.fromString("updated"),
            "createdAt" -> at.asJson,
            "updated" -> updated.asJson
          )
        case AppointTimeDeleted(at, deleted) =>
          Json.obj(
            "model" -> Json.fromString("appoint-time"),
            "kind" -> Json.fromString("deleted"),
            "createdAt" -> at.asJson,
            "deleted" -> deleted.asJson
          )
        case HotlineCreated(at, created) =>
          Json.obj(
            "model" -> Json.fromString("hotline"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "created" -> created.asJson
          )
        case HotlineBeep(at, recipient) =>
          Json.obj(
            "model" -> Json.fromString("hotline"),
            "kind" -> Json.fromString("beep"),
            "createdAt" -> at.asJson,
            "recipient" -> recipient.asJson
          )
        case UnknownAppEvent(appEventId, createdAt, model, kind, data) =>
          Json.obj(
            "appEventId" -> Json.fromInt(appEventId),
            "createdAt" -> createdAt.asJson,
            "model" -> Json.fromString(model),
            "kind" -> Json.fromString(kind),
            "data" -> Json.fromString(data)
          )
      }

  given Decoder[AppModelEvent] with
    def apply(c: HCursor): Decoder.Result[AppModelEvent] =
      val pre =
        for
          model <- c.downField("model").as[String]
          kind <- c.downField("kind").as[String]
          createdAt <- c.downField("createdAT").as[LocalDateTime]
        yield (model, kind, createdAt)
      pre.flatMap(tup =>
        tup match {
          case ("appoint", "created", at) =>
            for created <- c.downField("created").as[Appoint]
            yield AppointCreated(at, created)
          case ("appoint", "updated", at) =>
            for updated <- c.downField("updated").as[Appoint]
            yield AppointUpdated(at, updated)
          case ("appoint", "deleted", at) =>
            for deleted <- c.downField("deleted").as[Appoint]
            yield AppointDeleted(at, deleted)
          case ("appoint-time", "created", at) =>
            for created <- c.downField("created").as[AppointTime]
            yield AppointTimeCreated(at, created)
          case ("appoint-time", "updated", at) =>
            for updated <- c.downField("updated").as[AppointTime]
            yield AppointTimeUpdated(at, updated)
          case ("appoint-time", "deleted", at) =>
            for deleted <- c.downField("deleted").as[AppointTime]
            yield AppointTimeDeleted(at, deleted)
          case ("hotline", "created", at) =>
            for created <- c.downField("created").as[Hotline]
            yield HotlineCreated(at, created)
          case ("hotline", "beep", at) =>
            for recipient <- c.downField("recipient").as[String]
            yield HotlineBeep(at, recipient)
          case (model, kind, at) =>
            for
              appEventId <- c.downField("appEventId").as[Int]
              data <- c.downField("data").as[String]
            yield UnknownAppEvent(appEventId, at, model, kind, data)
        }
      )
