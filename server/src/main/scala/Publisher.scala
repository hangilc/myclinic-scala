package dev.myclinic.scala.server

import cats.effect._
import cats.syntax.all._
import io.circe._
import io.circe.syntax._
import fs2.concurrent.Topic
import org.http4s.websocket.WebSocketFrame
import dev.myclinic.scala.model.jsoncodec.EventType
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import org.http4s.websocket.WebSocketFrame.Text

trait Publisher:
  def publish(event: EventType)(using
      topic: Topic[IO, WebSocketFrame]
  ): IO[Unit] =
    topic.publish1(Text(event.asJson.toString)).void

  def publishAll(events: List[EventType])(using
      topic: Topic[IO, WebSocketFrame]
  ): IO[Unit] =
    events.map(publish(_)).sequence_

