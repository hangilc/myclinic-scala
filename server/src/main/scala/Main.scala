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
import org.http4s.headers.Location
import java.time.LocalDate
import java.time.LocalTime
import org.http4s.server.websocket.WebSocketBuilder
import fs2.concurrent.Topic
import fs2.Stream
import fs2.Pipe
import org.http4s.websocket.WebSocketFrame
import org.http4s.websocket.WebSocketFrame.Text

object Main extends IOApp {

  object apiServer
      extends server.Endpoints[IO]
      with ApiEndpoints
      with server.JsonEntitiesFromSchemas {
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
              Db.registerAppoint(date, time, name)
            }
        }.tupled),
        cancelAppoint.implementedByEffect({
          (date: LocalDate, time: LocalTime, name: String) =>
            {
              Db.cancelAppoint(date, time, name)
            }
        }.tupled),
        getAppoint.implementedByEffect({ (date: LocalDate, time: LocalTime) =>
          Db.getAppoint(date, time)
        }.tupled)
      )
    )
  }

  import cats.effect.unsafe.implicits.{global => g}
  val topic = Topic[IO, WebSocketFrame].unsafeRunSync()

  val ws = HttpRoutes.of[IO] { case GET -> Root / "echo" =>
    val toClient = topic.subscribe(10)
    val fromClient: Pipe[IO, WebSocketFrame, Unit] = _.evalMap {
      case Text(t, _) => IO.delay(println(t))
      case f          => IO.delay(println(s"Unknown type: $f"))
    }
    WebSocketBuilder[IO].build(toClient, fromClient)
  }

  val helloService = HttpRoutes.of[IO] {
    case GET -> Root => {
      val frame = Text("HELLO")
      val stream = Stream(frame).covary[IO]
      topic.publish(stream)
      println("sent to topic")
      Ok("api-hello")
    }
  }

  val staticService = fileService[IO](FileService.Config("./web", "/"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
        Router(
          "/appoint" -> HttpRoutes.of[IO] { case GET -> Root =>
            PermanentRedirect(Location(uri"/appoint/"))
          },
          "/api" -> apiServer.routes,
          "/ws" -> ws,
          "/hello" -> helloService,
          "/" -> staticService
        ).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
