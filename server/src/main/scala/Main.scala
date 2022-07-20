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
import com.typesafe.scalalogging.Logger
import dev.myclinic.scala.clinicop.ClinicOperation
import dev.myclinic.scala.config.Config
import org.http4s.server.websocket.WebSocketBuilder2

object Main extends IOApp:
  val logger = Logger(getClass.getName)

  object AppEventBroadcaster:
    def broadcast(
        topic: Topic[IO, WebSocketFrame],
        text: String
    ): IO[Unit] =
      topic.publish1(Text(text)) >> IO.pure(())

  def ws(topic: Topic[IO, WebSocketFrame], websocketBuilder: WebSocketBuilder2[IO]) = HttpRoutes.of[IO] {
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
        resp <- websocketBuilder.build(toClient, fromClient)
      yield resp
  }

  val portalTmpStaticService = fileService[IO](
    FileService.Config(System.getenv("MYCLINIC_PORTAL_TMP_DIR"), "/")
  )
  val staticService = fileService[IO](FileService.Config("./web", "/"))

  def buildServer(
      topic: Topic[IO, WebSocketFrame],
      port: Int,
      sslContextOption: Option[SSLContext]
  ) =
    given Topic[IO, WebSocketFrame] = topic
    var builder = BlazeServerBuilder[IO]
      .withSocketReuseAddress(true)
      .bindHttp(port, "0.0.0.0")
    if sslContextOption.isDefined then
      builder = builder.withSslContext(sslContextOption.get)
    builder
      .withHttpWebSocketApp(websocketBuilder =>
        Router(
          // "/appoint" -> HttpRoutes.of[IO] { case GET -> Root =>
          //   TemporaryRedirect(Location(uri"/appoint/index.html"))
          // },
          // "/reception" -> HttpRoutes.of[IO] { case GET -> Root =>
          //   TemporaryRedirect(Location(uri"/reception/index.html"))
          // },
          // "/practice" -> HttpRoutes.of[IO] { case GET -> Root =>
          //   TemporaryRedirect(Location(uri"/practice/index.html"))
          // },
          "/api" -> RestService.routes,
          "/ws" -> ws(topic, websocketBuilder),
          "/portal-tmp" -> portalTmpStaticService,
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
      _ <- IO {
        ClinicOperation.setAdHocHolidayRanges(Config.adHocHolidayRanges)
      }
      _ <- IO { logger.info("Starting server.") }
      exitCode <- buildServer(topic, port, sslContextOption)
    yield exitCode
