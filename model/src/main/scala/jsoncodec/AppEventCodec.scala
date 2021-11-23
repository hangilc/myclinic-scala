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

  given Encoder[HotlineCreated] with
    def apply(e: HotlineCreated): Json = appModelEventEncoder(e)

  given appModelEventEncoder: Encoder[AppModelEvent] with
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
        case HotlineCreated(at, appEventId, created) =>
          Json.obj(
            "model" -> Json.fromString("hotline"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "appEventId" -> Json.fromInt(appEventId),
            "created" -> created.asJson
          )
        case HotlineBeep(at, recipient) =>
          Json.obj(
            "model" -> Json.fromString("hotline"),
            "kind" -> Json.fromString("beep"),
            "createdAt" -> at.asJson,
            "recipient" -> recipient.asJson
          )
        case VisitCreated(at, created) =>
          Json.obj(
            "model" -> Json.fromString("visit"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "created" -> created.asJson
          )
        case VisitUpdated(at, updated) =>
          Json.obj(
            "model" -> Json.fromString("visit"),
            "kind" -> Json.fromString("updated"),
            "createdAt" -> at.asJson,
            "updated" -> updated.asJson
          )
        case VisitDeleted(at, deleted) =>
          Json.obj(
            "model" -> Json.fromString("visit"),
            "kind" -> Json.fromString("deleted"),
            "createdAt" -> at.asJson,
            "deleted" -> deleted.asJson
          )
        case WqueueCreated(at, created) =>
          Json.obj(
            "model" -> Json.fromString("wqueue"),
            "kind" -> Json.fromString("created"),
            "createdAt" -> at.asJson,
            "created" -> created.asJson
          )
        case WqueueUpdated(at, updated) =>
          Json.obj(
            "model" -> Json.fromString("wqueue"),
            "kind" -> Json.fromString("updated"),
            "createdAt" -> at.asJson,
            "updated" -> updated.asJson
          )
        case WqueueDeleted(at, deleted) =>
          Json.obj(
            "model" -> Json.fromString("wqueue"),
            "kind" -> Json.fromString("deleted"),
            "createdAt" -> at.asJson,
            "deleted" -> deleted.asJson
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

  given Decoder[HotlineCreated] = appModelEventDecoder.map(
    evt => evt.asInstanceOf[HotlineCreated]
  )

  given appModelEventDecoder: Decoder[AppModelEvent] with
    def apply(c: HCursor): Decoder.Result[AppModelEvent] =
      val pre =
        for
          model <- c.downField("model").as[String]
          kind <- c.downField("kind").as[String]
          createdAt <- c.downField("createdAt").as[LocalDateTime]
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
            for 
              created <- c.downField("created").as[Hotline]
              appEventId <- c.downField("appEventId").as[Int]
            yield HotlineCreated(at, appEventId, created)
          case ("hotline", "beep", at) =>
            for recipient <- c.downField("recipient").as[String]
            yield HotlineBeep(at, recipient)
          case ("visit", "created", at) =>
            for created <- c.downField("created").as[Visit]
            yield VisitCreated(at, created)
          case ("visit", "updated", at) =>
            for updated <- c.downField("updated").as[Visit]
            yield VisitUpdated(at, updated)
          case ("visit", "deleted", at) =>
            for deleted <- c.downField("deleted").as[Visit]
            yield VisitDeleted(at, deleted)
          case (model, kind, at) =>
            for
              appEventId <- c.downField("appEventId").as[Int]
              data <- c.downField("data").as[String]
            yield UnknownAppEvent(appEventId, at, model, kind, data)
        }
      )
