package dev.myclinic.scala

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.blaze._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.io._
import org.http4s.headers.Location
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.staticcontent._

import java.io.File
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.ExecutionContextExecutorService

object Main extends IOApp {

  val apiService = HttpRoutes.of[IO] { case GET -> Root / "api" / "hello" =>
    Ok("api-hello")
  }

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
