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

object Main extends IOApp {

  val helloService = HttpRoutes.of[IO] { case GET -> Root / "hello" =>
    Ok("api-hello")
  }

  object apiServer extends server.Endpoints[IO]
      with ApiEndpoints
      with server.JsonEntitiesFromSchemas {
    val routes: HttpRoutes[IO] = HttpRoutes.of(
      routesFromEndpoints(
        listAppoint.implementedByEffect({
          (from: LocalDate, upto: LocalDate) => {
            Db.listAppoint(from, upto)
          }
        }.tupled),
        registerAppoint.implementedByEffect({
          (date: LocalDate, time: LocalTime, name: String) => {
            Db.registerAppoint(date, time, name)
          }
        }.tupled)
      )
    )
  }

  val staticService = fileService[IO](FileService.Config("./web", "/"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(Router(
        "/appoint" -> HttpRoutes.of[IO] {
          case GET -> Root => PermanentRedirect(Location(uri"/appoint/"))
        },
        "/api" -> apiServer.routes,
        "/" -> staticService
      ).orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
