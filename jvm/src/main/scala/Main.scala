package dev.myclinic.scala

import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.blaze._
import org.http4s.implicits._
import org.http4s.server._
import org.http4s.server.staticcontent._
import scala.concurrent.ExecutionContext.Implicits.global
import java.io.File
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContextExecutorService
import scala.concurrent.ExecutionContext
import org.http4s.headers.Location
import org.http4s.blaze.server.BlazeServerBuilder

object Main extends IOApp {

  val indexService = HttpRoutes.of[IO] { case GET -> Root =>
    MovedPermanently(Location(uri"/static/index.html"))
  }

  val apiService = HttpRoutes.of[IO] { case GET -> Root / "api" / "hello" =>
    Ok("api-hello")
  }

  val staticService = fileService[IO](FileService.Config("./web", "/static"))

  override def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO](global)
      .withSocketReuseAddress(true)
      .bindHttp(8080, "localhost")
      .withHttpApp(
          (indexService <+> apiService <+> staticService).orNotFound
      )
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
