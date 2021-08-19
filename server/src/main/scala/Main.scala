package dev.myclinic.scala.server

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.blaze._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.staticcontent._
import org.http4s.circe._

import java.io.File
import java.time.{LocalDate, LocalTime}
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContextExecutorService
import dev.myclinic.scala.db.Db

import io.circe.syntax._

object Main extends IOApp {

  // val apiService = HttpRoutes.of[IO] { 
  //   case GET -> Root / "api" / "hello" => Ok("api-hello")
  //   case GET -> Root / "api" / "get-appoint" =>
  //     Ok(Db.listAppoint(LocalDate.of(2021, 8, 1), LocalDate.of(2021, 8, 30))
  //       .map(_.asJson))
  // }

  val apiService = AppointService.service

  val staticService = fileService[IO](FileService.Config("./web", "/"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
          (apiService <+> staticService).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
