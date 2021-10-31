package dev.myclinic.scala.server

import cats.effect._
import fs2.Pipe
import fs2.concurrent.Topic
import org.http4s._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.staticcontent.FileService
import org.http4s.server.staticcontent.fileService
import org.http4s.server.websocket.WebSocketBuilder
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

import scala.concurrent.ExecutionContext.Implicits.global
object Main extends IOApp:

  object AppEventBroadcaster:
    def broadcast(
        topic: Topic[IO, WebSocketFrame],
        text: String
    ): IO[Unit] =
      topic.publish1(Text(text)) >> IO.pure(())

  def ws(topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case GET -> Root / "events" =>
      val toClient = topic
        .subscribe(10)
      val fromClient: Pipe[IO, WebSocketFrame, Unit] = _.evalMap {
        case Text(t, _) => IO.delay(println(t))
        case f          => IO.delay(println(s"Unknown type: $f"))
      }
      WebSocketBuilder[IO].build(toClient, fromClient)
  }

  def helloService() = HttpRoutes.of[IO] {
    case GET -> Root => {
      Ok("hello")
    }
  }

  val staticService = fileService[IO](FileService.Config("./web", "/"))

  def buildServer(topic: Topic[IO, WebSocketFrame]) =
    given Topic[IO, WebSocketFrame] = topic
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "0.0.0.0")
      .withHttpApp(
        Router(
          "/appoint" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/appoint/"))
          },
          "/api" -> RestService.routes,
          "/ws" -> ws(topic),
          "/" -> staticService
        ).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] =
    for
      topic <- Topic[IO, WebSocketFrame]
      exitCode <- buildServer(topic)
    yield exitCode

