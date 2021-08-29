package dev.myclinic.scala.server

import cats.effect._
import cats.implicits._
import org.http4s._

import scala.concurrent.ExecutionContext.Implicits.global
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.server.Router
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server.staticcontent.FileService
import org.http4s.server.staticcontent.fileService

import endpoints4s.http4s.server
import dev.myclinic.scala.api.AppointEndpoints
import java.time.{LocalDate, LocalTime}
import dev.myclinic.scala.model.Appoint
import dev.myclinic.scala.db.Db

object Main extends IOApp {

  val helloService = HttpRoutes.of[IO] { case GET -> Root / "hello" =>
    Ok("api-hello")
  }

  val apiService = helloService <+> AppointService.service

  object apiServer extends server.Endpoints[IO]
      with AppointEndpoints
      with server.JsonEntitiesFromSchemas {
    val routes: HttpRoutes[IO] = HttpRoutes.of(
      routesFromEndpoints(
        listAppoint.implementedByEffect({
          (from: LocalDate, upto: LocalDate) => {
            Db.listAppoint(from, upto)
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
      // .withHttpApp(
      //   Router("/api" -> apiService, 
      //   "/test/" -> apiServer.routes,
      //   "/" -> staticService).orNotFound
      // )
      .withHttpApp(Router(
        "/api" -> apiServer.routes
      ).orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
