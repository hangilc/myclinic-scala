package dev.myclinic.scala.server

import cats.*
import cats.syntax.all.*
import cats.effect._
import cats.effect.syntax.*
import cats.data.OptionT
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
import javax.net.ssl.SSLContext
import scala.concurrent.ExecutionContext.Implicits.global
import java.nio.file.Files
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model.EventIdNotice
import dev.myclinic.scala.model.jsoncodec.EventType
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import io.circe._
import io.circe.syntax._
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import dev.myclinic.scala.model.HeartBeat
import dev.myclinic.scala.model.jsoncodec.Implicits.given
import scala.concurrent.duration.DurationInt

object Main extends IOApp:

  object AppEventBroadcaster:
    def broadcast(
        topic: Topic[IO, WebSocketFrame],
        text: String
    ): IO[Unit] =
      topic.publish1(Text(text)) >> IO.pure(())

  def ws(topic: Topic[IO, WebSocketFrame]) = HttpRoutes.of[IO] {
    case GET -> Root / "events" =>
      for
        eventId <- Db.currentEventId()
        toClient = fs2.Stream[IO, WebSocketFrame](
          Text(EventIdNotice(eventId).asInstanceOf[EventType].asJson.toString)
        ) ++ topic.subscribe(10)
        fromClient = (s: fs2.Stream[IO, WebSocketFrame]) =>
          s.evalMap {
            case Text("heart-beat", _) =>
              IO.delay(
                // println("heart-beat received")
                ()
              )
            case Text(t, _) => IO.delay(println(t))
            case f          => IO.delay(println(s"Unknown type: $f"))
          }
        resp <- WebSocketBuilder[IO].build(toClient, fromClient)
      yield resp
  }

  val staticService = fileService[IO](FileService.Config("./web", "/"))
  val deployTestService: HttpRoutes[IO] =
    if Files.exists(java.nio.file.Path.of("./deploy")) then
      fileService[IO](FileService.Config("./deploy", "/"))
    else HttpRoutes[IO](_ => OptionT.some(Response.notFound[IO]))

  def buildServer(
      topic: Topic[IO, WebSocketFrame],
      port: Int,
      sslContextOption: Option[SSLContext]
  ) =
    given Topic[IO, WebSocketFrame] = topic
    var builder = BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(port, "0.0.0.0")
    if sslContextOption.isDefined then
      builder = builder.withSslContext(sslContextOption.get)
    builder
      .withHttpApp(
        Router(
          "/appoint" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/appoint/"))
          },
          "/reception" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/reception/"))
          },
          "/practice" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/practice/"))
          },
          "/api" -> RestService.routes,
          "/ws" -> ws(topic),
          "/deploy" -> deployTestService,
          "/" -> staticService
        ).orNotFound
      )
      .resource
      .use(_ => {
        val heartBeatFrame =
          Text(HeartBeat().asInstanceOf[EventType].asJson.toString)
        fs2.Stream
          .awakeEvery[IO](15.seconds)
          .evalMap(_ => {
            topic.publish1(heartBeatFrame)
          })
          .compile
          .drain
      })
      .flatMap(_ => IO.never)
      //.use(_ => IO.never)
      .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] =
    val cmdArgs = CmdArgs(args)
    val port = if cmdArgs.ssl then 8443 else 8080
    val sslContextOption: Option[SSLContext] =
      if cmdArgs.ssl then
        Some(
          Ssl.createContext(
            System.getenv("MYCLINIC_SERVER_CERT_KEYSTORE"),
            System.getenv("MYCLINIC_SERVER_CERT_KEYSTORE_PASS")
          )
        )
      else None
    for
      topic <- Topic[IO, WebSocketFrame]
      // _ <- fs2.Stream.awakeEvery[IO](15.seconds).map(_ => {
      //   topic.publish1(heartBeatFrame)
      // }).compile.drain
      exitCode <- buildServer(topic, port, sslContextOption)
    yield exitCode
