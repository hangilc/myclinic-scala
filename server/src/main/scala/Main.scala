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
import javax.net.ssl.SSLContext

import scala.concurrent.ExecutionContext.Implicits.global
import java.nio.file.Files
import cats.data.OptionT
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
          "/api" -> RestService.routes,
          "/ws" -> ws(topic),
          "/deploy" -> deployTestService,
          "/" -> staticService
        ).orNotFound
      )
      .resource
      .use(_ => IO.never)
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
      exitCode <- buildServer(topic, port, sslContextOption)
    yield exitCode
