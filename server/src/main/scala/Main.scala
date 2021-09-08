package dev.myclinic.scala.server

import cats.effect._
import org.http4s._

import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.staticcontent.FileService
import org.http4s.server.staticcontent.fileService

import endpoints4s.http4s.server
import dev.myclinic.scala.api.ApiEndpoints
import dev.myclinic.scala.db.Db
import dev.myclinic.scala.model._
import org.http4s.headers.Location
import java.time.LocalDate
import java.time.LocalTime
import org.http4s.server.websocket.WebSocketBuilder
import fs2.concurrent.Topic
import fs2.Pipe
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text
import endpoints4s.Valid
import endpoints4s.Invalid

object Main extends IOApp {

  object AppEventBroadcaster {
    def broadcast(
        topic: Topic[IO, WebSocketFrame],
        text: String
    ): IO[Unit] = {
      topic.publish1(Text(text)) >> IO.pure(())
    }
  }

  class ApiService(topic: Topic[IO, WebSocketFrame])
      extends server.Endpoints[IO]
      with ApiEndpoints
      with server.JsonEntitiesFromSchemas { self =>
    implicit val dbJsonEncoder = DbJsonEncoder
    val routes: HttpRoutes[IO] = HttpRoutes.of(
      routesFromEndpoints(
        listAppoint.implementedByEffect({ (from: LocalDate, upto: LocalDate) =>
          {
            Db.listAppoint(from, upto)
          }
        }.tupled),
        registerAppoint.implementedByEffect({
          (date: LocalDate, time: LocalTime, name: String) =>
            {
              val app = Appoint(date, time, 0, name, 0, "")
              for {
                appEvent <- Db.registerAppoint(app)
                _ <- AppEventBroadcaster.broadcast(
                  topic,
                  ServerJsonCodec.toJson(appEvent)
                )
              } yield ()
            }
        }.tupled),
        cancelAppoint.implementedByEffect({
          (date: LocalDate, time: LocalTime, name: String) =>
            for {
              appEvent <- Db.cancelAppoint(date, time, name)
              _ <- AppEventBroadcaster.broadcast(
                topic,
                ServerJsonCodec.toJson(appEvent)
              ) 
            } yield ()
        }.tupled),
        getAppoint.implementedByEffect({ (date: LocalDate, time: LocalTime) =>
          Db.getAppoint(date, time)
        }.tupled),
        getNextEventId.implementedByEffect(_ => Db.nextGlobalEventId())
      )
    )

    def toJson[T](value: T)(implicit schema: JsonSchema[T]): String = {
      schema.encoder.encode(value).toString()
    }

    def fromJson[T](src: String)(implicit schema: JsonSchema[T]): T = {
      schema.stringCodec.decode(src) match {
        case Valid(t)   => t
        case Invalid(e) => throw new RuntimeException(e.toString())
      }
    }

  }

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

  def buildServer(topic: Topic[IO, WebSocketFrame]) = {
    val apiService = new ApiService(topic)
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
        Router(
          "/appoint" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/appoint/"))
          },
          "/api" -> apiService.routes,
          "/ws" -> ws(topic),
          "/hello" -> helloService(),
          "/" -> staticService
        ).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      topic <- Topic[IO, WebSocketFrame]
      exitCode <- buildServer(topic)
    } yield exitCode
  }

}
